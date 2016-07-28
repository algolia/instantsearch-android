package com.algolia.instantsearch;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.View;

import com.algolia.instantsearch.model.Errors;
import com.algolia.instantsearch.model.Facet;
import com.algolia.instantsearch.views.AlgoliaResultsListener;
import com.algolia.instantsearch.views.RefinementList;
import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Searcher {

    private Index index;
    private static Client client; //TODO: Discuss: get/set for client/index?
    private Query query;

    private Runnable progressStartRunnable;
    private Runnable progressStopRunnable;
    private int progressStartDelay;

    private final List<AlgoliaResultsListener> resultsListeners = new ArrayList<>();

    private int lastSearchSeqNumber; // Identifier of last fired query
    private int lastDisplayedSeqNumber; // Identifier of last displayed query
    private int lastRequestedPage;
    private int lastDisplayedPage;
    private boolean endReached;

    private final Map<String, Pair<Integer, List<String>>> refinementMap = new HashMap<>();
    private final List<Integer> pendingRequests = new ArrayList<>();
    private final List<Integer> cancelledRequests = new ArrayList<>();

    /**
     * Create and initialize the helper.
     *
     * @param client a Client instance which will handle network requests.
     * @param index  an Index initialized and eventually configured.
     */
    public Searcher(final Client client, final Index index) {
        query = new Query();
        Searcher.client = client;
        this.index = index;
    }

    /**
     * Start a search with the given text.
     *
     * @param queryString a String to search on the index.
     */
    public Searcher search(final String queryString) {
        query.setQuery(queryString);
        search();
        return this;
    }

    /**
     * Start a search with the current helper's state.
     */
    public Searcher search() {
        endReached = false;
        lastRequestedPage = 0;
        lastDisplayedPage = -1;
        final int currentSearchSeqNumber = ++lastSearchSeqNumber;
        pendingRequests.add(currentSearchSeqNumber);
        final Handler progressHandler = new Handler(Looper.getMainLooper());
        if (progressStartRunnable != null) {
            progressHandler.postDelayed(progressStartRunnable, progressStartDelay);
        }

        index.searchAsync(query, new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject content, AlgoliaException error) {
                pendingRequests.remove(Integer.valueOf(currentSearchSeqNumber));
                if (progressStartRunnable != null) {
                    progressHandler.removeCallbacks(progressStartRunnable);
                }
                if (progressStopRunnable != null) {
                    new Handler().post(progressStopRunnable);
                }
                // NOTE: Check that the received results are newer that the last displayed results.
                //
                // Rationale: Although TCP imposes a server to send responses in the same order as
                // requests, nothing prevents the system from opening multiple connections to the
                // same server, nor the Algolia client to transparently switch to another server
                // between two requests. Therefore the order of responses is not guaranteed.
                if (currentSearchSeqNumber <= lastDisplayedSeqNumber || cancelledRequests.contains(currentSearchSeqNumber)) {
                    return;
                }

                if (content == null || !hasHits(content)) {
                    endReached = true;
                } else {
                    checkIfLastPage(content);
                }

                lastDisplayedSeqNumber = currentSearchSeqNumber;
                lastDisplayedPage = 0;

                if (error != null) {
                    for (AlgoliaResultsListener view : resultsListeners) {
                        view.onError(query, error);
                    }
                    Log.e("PLN|search.searchError", String.format("Index %s with query %s failed: %s(%s).", index.getIndexName(), query, error.getCause(), error.getMessage()));
                } else {
                    updateListeners(content, false);
                    Log.d("PLN|search.searchResult", String.format("Index %s with query %s succeeded: %s.", index.getIndexName(), query, content));
                }
            }
        });
        return this;
    }

    /**
     * Load more results with the same query.
     */
    public Searcher loadMore() {
        Query loadMoreQuery = new Query(query);
        loadMoreQuery.setPage(++lastRequestedPage);
        final int currentSearchSeqNumber = ++lastSearchSeqNumber;
        pendingRequests.add(currentSearchSeqNumber);
        index.searchAsync(loadMoreQuery, new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject content, AlgoliaException error) {
                pendingRequests.remove(Integer.valueOf(currentSearchSeqNumber));
                if (error != null) {
                    throw new RuntimeException(Errors.LOADMORE_FAIL, error);
                } else {
                    if (currentSearchSeqNumber <= lastDisplayedSeqNumber || cancelledRequests.contains(currentSearchSeqNumber)) {
                        return; // Hits are for an older query or a cancelled one, let's ignore them
                    }

                    if (hasHits(content)) {
                        updateListeners(content, true);
                        lastDisplayedPage = lastRequestedPage;

                        checkIfLastPage(content);
                    } else {
                        endReached = true;
                    }
                }
            }
        });
        return this;
    }

    private void checkIfLastPage(JSONObject content) {
        if (content.optInt("nbPages") == content.optInt("page") + 1) {
            endReached = true;
        }
    }

    /**
     * Tell if we should load more hits when reaching the end of an {@link AlgoliaResultsListener}.
     *
     * @return {@code true} unless we reached the end of hits or we already requested a new page.
     */
    public boolean shouldLoadMore() {
        return !(endReached || lastRequestedPage > lastDisplayedPage);
    }

    /**
     * Use the given query's parameters for following search queries.
     *
     * @param baseQuery a {@link Query} object with some parameters set.
     */
    public Searcher setBaseQuery(Query baseQuery) {
        query = baseQuery;
        return this;
    }

    /**
     * Reset the helper's state.
     */
    public Searcher reset() {
        lastDisplayedPage = 0;
        lastRequestedPage = 0;
        lastDisplayedSeqNumber = 0;
        lastSearchSeqNumber = 0;
        endReached = false;
        clearFacetRefinements();
        resetListeners();
        return this;
    }

    /**
     * Checks if some requests are still waiting for a response.
     *
     * @return true if there is at least one pending request.
     */
    public boolean hasPendingRequests() {
        return pendingRequests.size() != 0;
    }

    /**
     * Cancels all requests still waiting for a response.
     *
     * @return how many requests were cancelled.
     */
    public int cancelPendingRequests() {
        if (pendingRequests.size() != 0) {
            for (Integer reqId : pendingRequests) {
                cancelledRequests.add(reqId);
            }
        }
        return pendingRequests.size();
    }

    /**
     * Initialise a list of facet for the given widget's attribute and operator.
     *
     * @param widget a RefinementList to register as a source of facetRefinements.
     */
    public void registerRefinementList(RefinementList widget) {
        refinementMap.put(widget.getAttributeName(), new Pair<Integer, List<String>>(widget.getOperator(), new ArrayList<String>()));
    }


    /**
     * Add or remove this facet according to its enabled status.
     *
     * @param attributeName the attribute referenced by this facet.
     * @param facet         a Facet object to add to the query.
     */
    public Searcher updateFacetRefinement(String attributeName, Facet facet) {
        if (facet.isEnabled()) {
            addFacetRefinement(attributeName, facet.getName());
        } else {
            removeFacetRefinement(attributeName, facet.getName());
        }
        return this;
    }

    /**
     * Add a facet refinement and run again the current query.
     *
     * @param attribute the attribute to refine on.
     * @param value     the facet's value to refine with.
     */
    public Searcher addFacetRefinement(String attribute, String value) {
        refinementMap.get(attribute).second.add(value);
        rebuildQueryFacetRefinements();
        return this;
    }

    /**
     * Remove a facet refinement and run again the current query.
     *
     * @param attribute the attribute to refine on.
     * @param value     the facet's value to refine with.
     */
    public Searcher removeFacetRefinement(String attribute, String value) {
        refinementMap.get(attribute).second.remove(value);
        rebuildQueryFacetRefinements();
        return this;
    }

    /**
     * Check if a facet refinement is enabled.
     *
     * @param attribute the attribute to refine on.
     * @param value     the facet's value to check.
     * @return {@code true} if {@code attribute} is being refined with {@code value}.
     */
    public boolean hasFacetRefinement(String attribute, String value) {
        return refinementMap.get(attribute).second.contains(value);
    }


    /**
     * Clear all facet refinements.
     */
    public void clearFacetRefinements() {
        final Collection<Pair<Integer, List<String>>> values = refinementMap.values();
        for (Pair<Integer, List<String>> pair : values) {
            pair.second.clear();
        }
        rebuildQueryFacetRefinements();
    }

    /**
     * Clear an attribute's facet refinements.
     *
     * @param attribute the attribute's name.
     */
    public void clearFacetRefinements(String attribute) {
        refinementMap.get(attribute).second.clear();
        rebuildQueryFacetRefinements();
    }

    private void rebuildQueryFacetRefinements() {
        JSONArray facetFilters = new JSONArray();
        for (Map.Entry<String, Pair<Integer, List<String>>> entry : refinementMap.entrySet()) {
            final Pair<Integer, List<String>> pair = entry.getValue();
            final String attribute = entry.getKey();
            final Boolean operatorIsAnd = pair.first == RefinementList.OPERATOR_AND;
            final List<String> values = pair.second;

            if (operatorIsAnd) {
                for (String value : values) {
                    facetFilters.put(attribute + ":" + value);
                }
            } else {
                JSONArray attributeArray = new JSONArray();
                for (String value : values) {
                    attributeArray.put(attribute + ":" + value);
                }
                facetFilters.put(attributeArray);
            }
        }
        query.setFacetFilters(facetFilters);
    }

    void registerListener(AlgoliaResultsListener resultsListener) {
        if (!resultsListeners.contains(resultsListener)) {
            resultsListeners.add(resultsListener);
        }
    }

    void initResultsListeners() {
        for (AlgoliaResultsListener listener : resultsListeners) {
            listener.onInit(this);
        }
    }

    private void updateListeners(JSONObject hits, boolean isLoadingMore) {
        for (AlgoliaResultsListener view : resultsListeners) {
            view.onUpdateView(hits, isLoadingMore);
        }
    }

    void resetListeners() {
        for (AlgoliaResultsListener view : resultsListeners) {
            view.onReset();
        }
    }

    public Index getIndex() {
        return index;
    }

    /**
     * Change the targeted index for future queries.
     * Be aware that this method only changed the index without invalidating any existing state (pagination, facets, etc).
     * You may want to use {@link Searcher#reset} to reinitialize the helper to an empty state.
     *
     * @param indexName name of the new index.
     */
    public Searcher setIndex(String indexName) {
        index = client.initIndex(indexName);
        return this;
    }

    /**
     * Find the empty view in the given rootView.
     *
     * @param rootView the topmost view in the view hierarchy of the Activity.
     * @return the empty view if it was in the given rootView.
     * @throws IllegalStateException if no empty view can be found.
     */
    @NonNull
    private static View getEmptyView(View rootView) {
        View emptyView = rootView.findViewById(R.id.empty);
        if (emptyView == null) {
            throw new IllegalStateException(Errors.LAYOUT_MISSING_EMPTY);
        }
        return emptyView;
    }

    /**
     * Find if a returned json contains at least one hit.
     *
     * @param jsonObject the query result.
     * @return {@code true} if it contains a hits array with at least one non null element.
     */
    static boolean hasHits(@Nullable JSONObject jsonObject) {
        if (jsonObject == null) {
            return false;
        }

        JSONArray resultHits = jsonObject.optJSONArray("hits");
        if (resultHits == null) {
            return false;
        }

        for (int i = 0; i < resultHits.length(); ++i) {
            JSONObject hit = resultHits.optJSONObject(i);
            if (hit != null) {
                return true;
            }
        }
        return false;
    }

    public Query getQuery() {
        return query;
    }

    public void setProgressStartRunnable(Runnable progressStartRunnable) {
        this.progressStartRunnable = progressStartRunnable;
    }

    public void setProgressStopRunnable(Runnable progressStopRunnable) {
        this.progressStopRunnable = progressStopRunnable;
    }

    public void setProgressStartRunnable(Runnable runnable, int delay) {
        setProgressStartRunnable(runnable);
        progressStartDelay = delay;
    }
}
