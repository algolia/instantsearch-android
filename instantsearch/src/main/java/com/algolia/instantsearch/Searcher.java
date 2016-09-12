package com.algolia.instantsearch;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.algolia.instantsearch.events.CancelEvent;
import com.algolia.instantsearch.events.ErrorEvent;
import com.algolia.instantsearch.events.ResultEvent;
import com.algolia.instantsearch.events.SearchEvent;
import com.algolia.instantsearch.model.Errors;
import com.algolia.instantsearch.model.SearchResults;
import com.algolia.instantsearch.strategies.SearchStrategy;
import com.algolia.instantsearch.views.AlgoliaResultsListener;
import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.algolia.search.saas.Request;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Searcher {

    private final EventBus bus;
    private Index index;
    private final Client client;
    private Query query;

    @Nullable
    private Runnable progressStartRunnable;
    @Nullable
    private Runnable progressStopRunnable;
    private int progressStartDelay;

    private final List<AlgoliaResultsListener> resultsListeners = new ArrayList<>();
    private SearchStrategy strategy;

    private static int lastSearchSeqNumber; // Identifier of last fired query
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
     * @param index an Index initialized and eventually configured.
     */
    public Searcher(@NonNull final Index index) {
        query = new Query();
        this.index = index;
        this.client = index.getClient();
        bus = EventBus.getDefault();
    }

    /**
     * Start a search with the given text.
     *
     * @param queryString a String to search on the index.
     */
    @NonNull
    public Searcher search(final String queryString) {
        query.setQuery(queryString);
        search();
        return this;
    }

    /**
     * Start a search with the current helper's state, eventually checking the {{@link SearchStrategy}}.
     */
    @NonNull
    public Searcher search() {
        if (strategy != null) {
            if (!strategy.search(this, query.getQuery())) {
                return this;
            }
        }

        endReached = false;
        lastRequestedPage = 0;
        lastDisplayedPage = -1;
        final int currentSearchSeqNumber = ++lastSearchSeqNumber;
        final Handler progressHandler = new Handler(Looper.getMainLooper());
        if (progressStartRunnable != null) {
            progressHandler.postDelayed(progressStartRunnable, progressStartDelay);
        }

        bus.post(new SearchEvent(query, currentSearchSeqNumber));
        final CompletionHandler searchHandler = new CompletionHandler() {
            @Override
            public void requestCompleted(@Nullable JSONObject content, @Nullable AlgoliaException error) {
                pendingRequests.remove(currentSearchSeqNumber);
                if (progressStartRunnable != null) {
                    progressHandler.removeCallbacks(progressStartRunnable);
                }
                if (progressStopRunnable != null) {
                    new Handler().post(progressStopRunnable);
                }
                // NOTE: Canceling any request anterior to the current one.
                //
                // Rationale: Although TCP imposes a server to send responses in the same order as
                // requests, nothing prevents the system from opening multiple connections to the
                // same server, nor the Algolia client to transparently switch to another server
                // between two requests. Therefore the order of responses is not guaranteed.
                for (Map.Entry<Integer, Request> entry : pendingRequests.entrySet()) {
                    if (entry.getKey() < currentSearchSeqNumber) {
                        cancelRequest(entry.getValue(), entry.getKey());
                    }
                }

                if (currentSearchSeqNumber <= lastDisplayedSeqNumber) {
                    throw new IllegalStateException("This request should have been cancelled.");
                }

                if (content == null || !hasHits(content)) {
                    endReached = true;
                } else {
                    checkIfLastPage(content);
                }

                lastDisplayedSeqNumber = currentSearchSeqNumber;
                lastDisplayedPage = 0;

                if (error != null) {
                    bus.post(new ErrorEvent(error, query, currentSearchSeqNumber));
                    for (AlgoliaResultsListener view : resultsListeners) {
                        view.onError(query, error);
                    }
                } else {
                    bus.post(new ResultEvent(content, query, currentSearchSeqNumber));
                    updateListeners(content, false);
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

    private void cancelRequest(Request request, Integer requestSeqNumber) {
        request.cancel();
        bus.post(new CancelEvent(request, requestSeqNumber));
    }

    /**
     * Load more results with the same query.
     * Note that this method won't do anything if {@link Searcher#shouldLoadMore} returns false.
     */
    @NonNull
    public Searcher loadMore() {
        if (!shouldLoadMore()) {
            return this;
        }
        Query loadMoreQuery = new Query(query);
        loadMoreQuery.setPage(++lastRequestedPage);
        final int currentSearchSeqNumber = ++lastSearchSeqNumber;
        pendingRequests.put(currentSearchSeqNumber, index.searchAsync(loadMoreQuery, new CompletionHandler() {
            @Override
            public void requestCompleted(@NonNull JSONObject content, @Nullable AlgoliaException error) {
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
    @NonNull
    public Searcher reset() {
        lastDisplayedPage = 0;
        lastRequestedPage = 0;
        lastDisplayedSeqNumber = 0;
        endReached = false;
        clearFacetRefinements();
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
     */
    public Searcher cancelPendingRequests() {
        if (pendingRequests.size() != 0) {
            for (Map.Entry<Integer, Request> entry : pendingRequests.entrySet()) {
                Request r = entry.getValue();
                if (!r.isFinished() && !r.isCancelled()) {
                    cancelRequest(r, entry.getKey());
                }
            }
        }
        return this;
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
     * @param attributeName the attribute to facet on.
     * @param value         the value for this attribute.
     * @param active        {@code true} if this facet value is currently refined on.
     */
    @NonNull
    public Searcher updateFacetRefinement(@NonNull String attributeName, @NonNull String value, boolean active) {
        if (active) {
            addFacetRefinement(attributeName, value);
        } else {
            removeFacetRefinement(attributeName, value);
        }
        return this;
    }


    /**
     * Add a facet refinement and run again the current query.
     *
     * @param attributeName the attribute to refine on.
     * @param value         the facet's value to refine with.
     */
    @NonNull
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
    @NonNull
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
    public Searcher clearFacetRefinements() {
        refinementMap.clear();
        disjunctiveFacets.clear();
        rebuildQueryFacetRefinements();
        return this;
    }


    /**
     * Clear an attribute's facet refinements.
     *
     * @param attribute the attribute's name.
     */
    public Searcher clearFacetRefinements(@NonNull String attribute) {
        final List<String> stringList = refinementMap.get(attribute);
        if (stringList != null) {
            stringList.clear();
        }
        disjunctiveFacets.remove(attribute);
        rebuildQueryFacetRefinements();
        return this;
    }

    private Searcher rebuildQueryFacetRefinements() {
        JSONArray facetFilters = new JSONArray();
        for (Map.Entry<String, List<String>> entry : refinementMap.entrySet()) {
            final List<String> values = entry.getValue();
            final String attribute = entry.getKey();

            for (String value : values) {
                facetFilters.put(attribute + ":" + value);
            }
        }
        query.setFacetFilters(facetFilters);
        return this;
    }

    public Searcher registerListener(@NonNull AlgoliaResultsListener resultsListener) {
        if (!resultsListeners.contains(resultsListener)) {
            resultsListeners.add(resultsListener);
        }
        return this;
    }

    private void updateListeners(@Nullable JSONObject hits, boolean isLoadingMore) {
        for (AlgoliaResultsListener view : resultsListeners) {
            view.onResults(new SearchResults(hits), isLoadingMore);
        }
    }

    public Searcher postErrorEvent(String cause) {
        bus.post(new ErrorEvent(new AlgoliaException(cause), query, lastSearchSeqNumber));
        return this;
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

    public Searcher setProgressStartRunnable(@Nullable Runnable progressStartRunnable) {
        this.progressStartRunnable = progressStartRunnable;
        return this;
    }

    public Searcher setProgressStopRunnable(@Nullable Runnable progressStopRunnable) {
        this.progressStopRunnable = progressStopRunnable;
        return this;
    }

    public Searcher setProgressStartRunnable(@Nullable Runnable runnable, int delay) {
        setProgressStartRunnable(runnable);
        progressStartDelay = delay;
        return this;
    }

    public Query getQuery() {
        return query;
    }

    /**
     * Use the given query's parameters for following search queries.
     *
     * @param query a {@link Query} object with some parameters set.
     */
    @NonNull
    public Searcher setQuery(@NonNull Query query) {
        this.query = query;
        return this;
    }

    public Index getIndex() {
        return index;
    }

    /**
     * Change the targeted index for future queries.
     * Be aware that as index ordering may differ, this method will reset the current page to 0,
     * You may want to use {@link Searcher#reset} to reinitialize the helper to an empty state.
     *
     * @param indexName name of the new index.
     */
    @NonNull
    public Searcher setIndex(@NonNull String indexName) {
        index = client.initIndex(indexName);
        query.setPage(0);
        return this;
    }

    public SearchStrategy getStrategy() {
        return strategy;
    }

    public Searcher setStrategy(SearchStrategy strategy) {
        this.strategy = strategy;
        return this;
    }

    public int getLastRequestNumber() {
        return lastSearchSeqNumber;
    }
}
