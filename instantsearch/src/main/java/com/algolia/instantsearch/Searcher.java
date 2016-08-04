package com.algolia.instantsearch;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.algolia.instantsearch.model.Errors;
import com.algolia.instantsearch.model.Facet;
import com.algolia.instantsearch.views.AlgoliaResultsListener;
import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.algolia.search.saas.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Searcher {

    private Index index;
    private Client client;
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

    private final List<String> disjunctiveFacets = new ArrayList<>();
    private final Map<String, List<String>> refinementMap = new HashMap<>();

    private final Map<Integer, Request> pendingRequests = new HashMap<>();

    /**
     * Create and initialize the helper.
     *
     * @param client a Client instance which will handle network requests.
     * @param index  an Index initialized and eventually configured.
     */
    public Searcher(@NonNull final Client client, @NonNull final Index index) {
        query = new Query();
        this.client = client;
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
        final Handler progressHandler = new Handler(Looper.getMainLooper());
        if (progressStartRunnable != null) {
            progressHandler.postDelayed(progressStartRunnable, progressStartDelay);
        }

        final CompletionHandler searchHandler = new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject content, AlgoliaException error) {
                pendingRequests.remove(currentSearchSeqNumber);
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
                if (currentSearchSeqNumber <= lastDisplayedSeqNumber) {
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
        };

        final Request searchRequest;
        if (disjunctiveFacets.size() != 0) {
            searchRequest = index.searchDisjunctiveFacetingAsync(query, disjunctiveFacets, refinementMap, searchHandler);
        } else {
            searchRequest = index.searchAsync(query, searchHandler);
        }
        pendingRequests.put(currentSearchSeqNumber, searchRequest);
        return this;
    }

    /**
     * Load more results with the same query.
     * Note that this method won't do anything if {@link Searcher#shouldLoadMore} returns false.
     */
    public Searcher loadMore() {
        if (!shouldLoadMore()) {
            return this;
        }

        Query loadMoreQuery = new Query(query);
        loadMoreQuery.setPage(++lastRequestedPage);
        final int currentSearchSeqNumber = ++lastSearchSeqNumber;
        pendingRequests.put(currentSearchSeqNumber, index.searchAsync(loadMoreQuery, new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject content, AlgoliaException error) {
                pendingRequests.remove(currentSearchSeqNumber);
                if (error != null) {
                    throw new RuntimeException(Errors.LOADMORE_FAIL, error);
                } else {
                    if (currentSearchSeqNumber <= lastDisplayedSeqNumber) {
                        return; // Hits are for an older query, let's ignore them
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
        }));
        return this;
    }

    private void checkIfLastPage(@NonNull JSONObject content) {
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
        cancelPendingRequests();
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
            for (Request r : pendingRequests.values()) {
                if (!r.isFinished() && !r.isCancelled()) {
                    r.cancel();
                }
            }
        }
        return pendingRequests.size();
    }

    void addFacet(@NonNull String attributeName, boolean isDisjunctiveFacet, @Nullable ArrayList<String> values) {
        if (isDisjunctiveFacet) {
            disjunctiveFacets.add(attributeName);
        }
        if (values == null) {
            values = new ArrayList<>();
        }
        refinementMap.put(attributeName, values);
    }

    /**
     * Add or remove this facet according to its enabled status.
     *
     * @param attributeName the attribute referenced by this facet.
     * @param facet         a Facet object to add to the query.
     */
    public Searcher updateFacetRefinement(@NonNull String attributeName, @NonNull Facet facet) {
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
     * @param attributeName the attribute to refine on.
     * @param value         the facet's value to refine with.
     */
    public Searcher addFacetRefinement(@NonNull String attributeName, @NonNull String value) {
        List<String> attributeRefinements = refinementMap.get(attributeName);
        if (attributeRefinements == null) {
            attributeRefinements = new ArrayList<>();
            refinementMap.put(attributeName, attributeRefinements);
        }
        attributeRefinements.add(value);
        rebuildQueryFacetRefinements();
        return this;
    }

    /**
     * Remove a facet refinement and run again the current query.
     *
     * @param attributeName the attribute to refine on.
     * @param value         the facet's value to refine with.
     */
    public Searcher removeFacetRefinement(@NonNull String attributeName, @NonNull String value) {
        List<String> attributeRefinements = refinementMap.get(attributeName);
        if (attributeRefinements == null) {
            attributeRefinements = new ArrayList<>();
            refinementMap.put(attributeName, attributeRefinements);
        }
        attributeRefinements.remove(value);
        rebuildQueryFacetRefinements();
        return this;
    }

    /**
     * Check if a facet refinement is enabled.
     *
     * @param attributeName the attribute to refine on.
     * @param value         the facet's value to check.
     * @return {@code true} if {@code attributeName} is being refined with {@code value}.
     */
    public boolean hasFacetRefinement(@NonNull String attributeName, @NonNull String value) {
        List<String> attributeRefinements = refinementMap.get(attributeName);
        return attributeRefinements != null && attributeRefinements.contains(value);
    }

    /**
     * Clear all facet refinements.
     */
    public void clearFacetRefinements() {
        refinementMap.clear();
        disjunctiveFacets.clear();
        rebuildQueryFacetRefinements();
    }


    /**
     * Clear an attribute's facet refinements.
     *
     * @param attribute the attribute's name.
     */
    public void clearFacetRefinements(@NonNull String attribute) {
        final List<String> stringList = refinementMap.get(attribute);
        if (stringList != null) {
            stringList.clear();
        }
        disjunctiveFacets.remove(attribute);
        rebuildQueryFacetRefinements();
    }

    private void rebuildQueryFacetRefinements() { //FIXME: use searchDisjunctiveFacetingAsync properly
        JSONArray facetFilters = new JSONArray();
        for (Map.Entry<String, List<String>> entry : refinementMap.entrySet()) {
            final List<String> values = entry.getValue();
            final String attribute = entry.getKey();

            for (String value : values) {
                facetFilters.put(attribute + ":" + value);
            }
        }
        query.setFacetFilters(facetFilters);
    }

    void registerListener(@NonNull AlgoliaResultsListener resultsListener) {
        if (!resultsListeners.contains(resultsListener)) {
            resultsListeners.add(resultsListener);
        }
    }

    void initResultsListeners() {
        for (AlgoliaResultsListener listener : resultsListeners) {
            listener.onInit(this);
        }
    }

    private void updateListeners(@Nullable JSONObject hits, boolean isLoadingMore) {
        for (AlgoliaResultsListener view : resultsListeners) {
            view.onUpdateView(hits, isLoadingMore);
        }
    }

    void resetListeners() {
        for (AlgoliaResultsListener view : resultsListeners) {
            view.onReset();
        }
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

    public void setProgressStartRunnable(@Nullable Runnable progressStartRunnable) {
        this.progressStartRunnable = progressStartRunnable;
    }

    public void setProgressStopRunnable(@Nullable Runnable progressStopRunnable) {
        this.progressStopRunnable = progressStopRunnable;
    }

    public void setProgressStartRunnable(@Nullable Runnable runnable, int delay) {
        setProgressStartRunnable(runnable);
        progressStartDelay = delay;
    }

    /**
     * Use the given query's parameters for following search queries.
     *
     * @param baseQuery a {@link Query} object with some parameters set.
     */
    public Searcher setBaseQuery(@NonNull Query baseQuery) {
        query = baseQuery;
        return this;
    }

    public Index getIndex() {
        return index;
    }

    /**
     * Change the targeted index for future queries. //TODO: Discuss with JS: Do you support this?
     * Be aware that this method only changed the index without invalidating any existing state (pagination, facets, etc).
     * You may want to use {@link Searcher#reset} to reinitialize the helper to an empty state.
     *
     * @param indexName name of the new index.
     */
    public Searcher setIndex(@NonNull String indexName) {
        index = client.initIndex(indexName);
        return this;
    }

    public Query getQuery() {
        return query;
    }
}
