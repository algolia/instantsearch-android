package com.algolia.instantsearch;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.algolia.instantsearch.events.CancelEvent;
import com.algolia.instantsearch.events.ErrorEvent;
import com.algolia.instantsearch.events.ResultEvent;
import com.algolia.instantsearch.events.SearchEvent;
import com.algolia.instantsearch.model.FacetStat;
import com.algolia.instantsearch.model.NumericFilter;
import com.algolia.instantsearch.model.SearchResults;
import com.algolia.instantsearch.strategies.AlwaysSearchStrategy;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Searcher {
    private static List<Searcher> instances = new ArrayList<>();
    private int id;

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
    private SearchStrategy strategy = new AlwaysSearchStrategy();

    private static int lastSearchSeqNumber; // Identifier of last fired query
    private int lastDisplayedSeqNumber; // Identifier of last displayed query
    private int lastRequestedPage;
    private int lastDisplayedPage;
    private boolean endReached;

    private final List<String> disjunctiveFacets = new ArrayList<>();
    private final Map<String, List<String>> refinementMap = new HashMap<>();
    private final Map<String, NumericFilter> numericFilterMap = new HashMap<>();
    private final Map<String, Boolean> booleanFilterMap = new HashMap<>();

    private List<String> facets = new ArrayList<>();
    private final Map<String, FacetStat> facetStats = new HashMap<>();

    private final SparseArray<Request> pendingRequests = new SparseArray<>();

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
        id = instances.size();
        instances.add(this);
    }

    public static Searcher get(int id) {
        return instances.get(id);
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
            if (!strategy.beforeSearch(this, query.getQuery())) {
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
                    progressHandler.post(progressStopRunnable);
                }
                // NOTE: Canceling any request anterior to the current one.
                //
                // Rationale: Although TCP imposes a server to send responses in the same order as
                // requests, nothing prevents the system from opening multiple connections to the
                // same server, nor the Algolia client to transparently switch to another server
                // between two requests. Therefore the order of responses is not guaranteed.
                for (int i = 0; i < pendingRequests.size(); i++) {
                    int reqId = pendingRequests.keyAt(i);
                    Request request = pendingRequests.valueAt(i);
                    if (reqId < currentSearchSeqNumber) {
                        cancelRequest(request, reqId);
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
                    updateFacetStats(content);
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
        final Handler progressHandler = new Handler(Looper.getMainLooper());
        if (progressStartRunnable != null) {
            progressHandler.postDelayed(progressStartRunnable, progressStartDelay);
        }
        bus.post(new SearchEvent(query, currentSearchSeqNumber));
        pendingRequests.put(currentSearchSeqNumber, index.searchAsync(loadMoreQuery, new CompletionHandler() {
            @Override
            public void requestCompleted(@NonNull JSONObject content, @Nullable AlgoliaException error) {
                pendingRequests.remove(currentSearchSeqNumber);
                if (progressStartRunnable != null) { // TODO: Display differently loadMore?
                    progressHandler.removeCallbacks(progressStartRunnable);
                }
                if (progressStopRunnable != null) {
                    new Handler().post(progressStopRunnable);
                }
                if (error != null) {
                    bus.post(new ErrorEvent(error, query, currentSearchSeqNumber));
                    for (AlgoliaResultsListener view : resultsListeners) {
                        view.onError(query, error);
                    }
                } else {
                    if (currentSearchSeqNumber <= lastDisplayedSeqNumber) {
                        return; // Hits are for an older query, let's ignore them
                    }

                    if (hasHits(content)) {
                        updateListeners(content, true);
                        updateFacetStats(content);
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
        numericFilterMap.clear();
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
            for (int i = 0; i < pendingRequests.size(); i++) {
                int reqId = pendingRequests.keyAt(i);
                Request r = pendingRequests.valueAt(i);
                if (!r.isFinished() && !r.isCancelled()) {
                    cancelRequest(r, reqId);
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
        rebuildQueryFacetFilters();
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
        rebuildQueryFacetFilters();
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
        rebuildQueryFacetFilters();
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
        rebuildQueryFacetFilters();
        return this;
    }

    private Searcher rebuildQueryFacetFilters() {
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

    public NumericFilter getNumericFilter(String attribute) {
        return numericFilterMap.get(attribute);
    }

    public Searcher addNumericFilter(NumericFilter filter) { //DISCUSS: set semantics?
        numericFilterMap.put(filter.attribute, filter);
        rebuildQueryFilters();
        return this;
    }

    private void rebuildQueryFilters() {
        StringBuilder filters = new StringBuilder();
        for (NumericFilter numericFilter : numericFilterMap.values()) {
            if (filters.length() > 0) {
                filters.append(" AND ");
            }
            filters.append(numericFilter.toString());
        }
        for (Map.Entry<String, Boolean> entry : booleanFilterMap.entrySet()) {
            if (filters.length() > 0) {
                filters.append(" AND ");
            }
            filters.append(entry.getKey()).append(":").append(entry.getValue());
        }
        query.setFilters(filters.toString());
    }

    public Searcher addBooleanFilter(String attribute, Boolean value) {
        booleanFilterMap.put(attribute, value);
        rebuildQueryFilters();
        return this;
    }

    public
    @Nullable
    Boolean getBooleanFilter(String attribute) {
        return booleanFilterMap.get(attribute);
    }

    public Searcher removeBooleanFilter(String attribute) {
        booleanFilterMap.remove(attribute);
        rebuildQueryFilters();
        return this;
    }

    private void updateFacetStats(JSONObject content) {
        if (content == null) {
            return;
        }

        JSONObject facets = content.optJSONObject("facets");
        JSONObject facets_stats = content.optJSONObject("facets_stats");
        if (facets != null) {
            final Iterator<String> keys = facets.keys();
            while (keys.hasNext()) { // for each faceted attribute
                double min = Double.MAX_VALUE;
                double max = Double.MIN_VALUE;
                double sum = 0;
                double avg;

                String attribute = keys.next();
                if (facets_stats != null) {
                    JSONObject attributeStats = facets_stats.optJSONObject(attribute);
                    if (attributeStats != null) { // Numerical attribute, let's use existing facets_stats
                        try {
                            min = attributeStats.getDouble("min");
                            max = attributeStats.getDouble("max");
                            sum = attributeStats.getDouble("sum");
                            avg = attributeStats.getDouble("avg");
                            facetStats.put(attribute, new FacetStat(min, max, avg, sum));
                            continue;
                        } catch (JSONException ignored) {
                        }
                    }
                }

                JSONObject values = facets.optJSONObject(attribute);
                final Iterator<String> valueKeys = values.keys();
                while (valueKeys.hasNext()) { // for each facet value
                    String valueKey = valueKeys.next();

                    // if boolean, interpret as int, else continue
                    if (valueKey.equals("true") || valueKey.equals("false")) {
                        int attributeValue = valueKey.equals("false") ? 0 : 1;
                        if (attributeValue < min) {
                            min = attributeValue;
                        }
                        if (attributeValue > max) {
                            max = attributeValue;
                        }
                        sum += attributeValue;
                    }
                }
                if (min != Double.MAX_VALUE && max != Double.MIN_VALUE) {
                    avg = sum / values.length();
                    facetStats.put(attribute, new FacetStat(min, max, avg, sum));
                }
            }
        }
    }

    public
    @Nullable
    FacetStat getFacetStat(String attribute) {
        return facetStats.get(attribute);
    }

    public Searcher addFacet(String... attributes) {
        Collections.addAll(facets, attributes);
        rebuildQueryFacets();
        return this;
    }

    public Searcher removeFacet(String... attributes) {
        for (String attribute : attributes) {
            facets.remove(attribute);
        }
        rebuildQueryFacets();
        return this;
    }

    private Searcher rebuildQueryFacets() {
        final String[] facetArray = this.facets.toArray(new String[this.facets.size()]);
        query.setFacets(facetArray);
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

    @NonNull
    public SearchStrategy getStrategy() {
        return strategy;
    }

    public Searcher setStrategy(@NonNull SearchStrategy strategy) {
        if (strategy == null) {
            throw new IllegalStateException("You can't set a null strategy, use new AlwaysOnStrategy().");
        }
        this.strategy = strategy;
        return this;
    }

    public int getLastRequestNumber() {
        return lastSearchSeqNumber;
    }

    public int getId() {
        return id;
    }
}
