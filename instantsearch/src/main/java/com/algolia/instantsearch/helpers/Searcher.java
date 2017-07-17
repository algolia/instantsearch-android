package com.algolia.instantsearch.helpers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;

import com.algolia.instantsearch.BuildConfig;
import com.algolia.instantsearch.events.CancelEvent;
import com.algolia.instantsearch.events.ErrorEvent;
import com.algolia.instantsearch.events.NumericRefinementEvent;
import com.algolia.instantsearch.events.RefinementEvent.Operation;
import com.algolia.instantsearch.events.ResultEvent;
import com.algolia.instantsearch.events.SearchEvent;
import com.algolia.instantsearch.model.AlgoliaErrorListener;
import com.algolia.instantsearch.model.AlgoliaResultListener;
import com.algolia.instantsearch.model.FacetStat;
import com.algolia.instantsearch.model.NumericRefinement;
import com.algolia.instantsearch.model.SearchResults;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Handles the state of the search interface, wrapping an {@link Client Algolia API Client} and provide a level of abstraction over it.
 * <p>
 * The Searcher is responsible of interacting with the Algolia engine: when {@link Searcher#search()} is called,
 * the Searcher will fire a request with the current {@link Searcher#query}, and will forward the search results
 * (or error) to its {@link AlgoliaResultListener result listeners} (or {@link AlgoliaErrorListener error listeners}).
 */
@SuppressWarnings("UnusedReturnValue") // chaining
public class Searcher {
    private static final List<Searcher> instances = new ArrayList<>();
    private final int id;

    /** The EventBus which will propagate events. */
    private final EventBus bus;
    /** The {@link Index} targeted by this Searcher. */
    private Index index;
    /** The {@link Client API Client} used by this Searcher. */
    private final Client client;
    /** The current state of the search {@link Query}. */
    private Query query;

    /** The {@link AlgoliaResultListener listeners} that will receive search results. */
    private final List<AlgoliaResultListener> resultListeners = new ArrayList<>();

    /** The {@link AlgoliaErrorListener listeners} that will receive search results. */
    private final List<AlgoliaErrorListener> errorListeners = new ArrayList<>();

    /** The identifier of the last search request fired by this Searcher. */
    private static int lastRequestId;
    /** The identifier of the last search response propagated by this Searcher. */
    private int lastResponseId; // Identifier of last displayed query
    /** The page number of the last search request fired by this Searcher. */
    private int lastRequestPage;
    /** The page number of the last search response propagated by this Searcher. */
    private int lastResponsePage;

    /** Whether the end of the results has been reached for the current {@link Searcher#query}. */
    private boolean endReached;

    /** The List of attributes that will be treated as disjunctive facets. */
    private final List<String> disjunctiveFacets = new ArrayList<>();
    /** The Map associating attributes with their respective refinement value(s). */
    private final Map<String, List<String>> refinementMap = new HashMap<>();
    /** The Map associating attributes with their respective numeric refinement value(s). */
    private final Map<String, SparseArray<NumericRefinement>> numericRefinements = new HashMap<>();
    /** The Map associating attributes with their respective boolean refinement value(s). */
    private final Map<String, Boolean> booleanFilterMap = new HashMap<>();

    /** The List of attributes that will be used for faceting. */
    private final List<String> facets = new ArrayList<>();
    /** A Map of FacetStats updated with every response. */
    private final Map<String, FacetStat> facetStats = new HashMap<>();
    /** A Map to keep counts of facet additions, so we don't remove them unless we get as much removals. */
    private final HashMap<String, Integer> facetRequestCount = new HashMap<>();

    /** The SparseArray associating pending requests with their {@link Searcher#lastRequestId identifier}. */
    private final SparseArray<Request> pendingRequests = new SparseArray<>();

    /**
     * Constructs an helper from an existing {@link Index}.
     *
     * @param index an Index initialized and eventually configured.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public Searcher(@NonNull final Index index) {
        this.index = index;
        this.client = index.getClient();
        query = new Query();
        client.addUserAgent(new Client.LibraryVersion("InstantSearch Android", String.valueOf(BuildConfig.VERSION_NAME)));
        bus = EventBus.getDefault();
        id = instances.size();
        instances.add(this);
    }

    /**
     * Constructs an helper, creating its {@link Searcher#index} and {@link Searcher#client} with the given parameters.
     *
     * @param appId     Your Algolia Application ID.
     * @param apiKey    A search-only API Key. (never use API keys that could modify your records! see https://www.algolia.com/doc/guides/security/api-keys)
     * @param indexName An index to target.
     */
    public Searcher(@NonNull final String appId, @NonNull final String apiKey, @NonNull final String indexName) {
        this(new Client(appId, apiKey).getIndex(indexName));
    }

    @Deprecated //DISCUSS: Should we expose this?
    public static Searcher get(int id) {
        return instances.get(id);
    }

    /**
     * Starts a search with the given text.
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
     * Start a search with the current helper's state.
     */
    @NonNull
    public Searcher search() {
        endReached = false;
        lastRequestPage = 0;
        lastResponsePage = -1;
        final int currentRequestId = ++lastRequestId;

        bus.post(new SearchEvent(query, currentRequestId));
        final CompletionHandler searchHandler = new CompletionHandler() {
            @Override
            public void requestCompleted(@Nullable JSONObject content, @Nullable AlgoliaException error) {
                pendingRequests.remove(currentRequestId);
                // NOTE: Canceling any request anterior to the current one.
                //
                // Rationale: Although TCP imposes a server to send responses in the same order as
                // requests, nothing prevents the system from opening multiple connections to the
                // same server, nor the Algolia client to transparently switch to another server
                // between two requests. Therefore the order of responses is not guaranteed.
                for (int i = 0; i < pendingRequests.size(); i++) {
                    int reqId = pendingRequests.keyAt(i);
                    Request request = pendingRequests.valueAt(i);
                    if (reqId < currentRequestId) {
                        cancelRequest(request, reqId);
                    }
                }

                if (currentRequestId <= lastResponseId) {
                    Log.e("Algolia|Searcher", "We already displayed results for request " + lastResponseId
                            + ", current request (" + currentRequestId + ") should have been canceled");
                }

                if (content == null || !hasHits(content)) {
                    endReached = true;
                } else {
                    checkIfLastPage(content);
                }

                lastResponseId = currentRequestId;
                lastResponsePage = 0;

                if (error != null) {
                    bus.post(new ErrorEvent(error, query, currentRequestId));
                    for (AlgoliaErrorListener listener : errorListeners) {
                        listener.onError(query, error);
                    }
                } else {
                    if (content == null) {
                        Log.e("Algolia|Searcher", "content is null but error is not.");
                    } else {
                        bus.post(new ResultEvent(content, query, currentRequestId));
                        updateListeners(content, false);
                        updateFacetStats(content);
                    }
                }
            }
        };

        final Request searchRequest;
        if (disjunctiveFacets.size() != 0) {
            searchRequest = index.searchDisjunctiveFacetingAsync(query, disjunctiveFacets, refinementMap, searchHandler);
        } else {
            searchRequest = index.searchAsync(query, searchHandler);
        }
        pendingRequests.put(currentRequestId, searchRequest);
        return this;
    }

    /**
     * Loads more results with the same query.
     * <p>
     * Note that this method won't do anything if {@link Searcher#hasMoreHits} returns false.
     */
    @NonNull
    public Searcher loadMore() {
        if (!hasMoreHits()) {
            return this;
        }
        Query loadMoreQuery = new Query(query);
        loadMoreQuery.setPage(++lastRequestPage);
        final int currentRequestId = ++lastRequestId;
        bus.post(new SearchEvent(query, currentRequestId));
        pendingRequests.put(currentRequestId, index.searchAsync(loadMoreQuery, new CompletionHandler() {
            @Override
            public void requestCompleted(@NonNull JSONObject content, @Nullable AlgoliaException error) {
                pendingRequests.remove(currentRequestId);
                if (error != null) {
                    bus.post(new ErrorEvent(error, query, currentRequestId));
                    for (AlgoliaErrorListener listener : errorListeners) {
                        listener.onError(query, error);
                    }
                } else {
                    if (currentRequestId <= lastResponseId) {
                        return; // Hits are for an older query, let's ignore them
                    }

                    bus.post(new ResultEvent(content, query, currentRequestId));
                    if (hasHits(content)) {
                        updateListeners(content, true);
                        updateFacetStats(content);
                        lastResponsePage = lastRequestPage;

                        checkIfLastPage(content);
                    } else {
                        endReached = true;
                    }
                }
            }
        }));
        return this;
    }

    /**
     * Checks if we should load more hits when reaching the end of the current list of hits.
     *
     * @return {@code true} unless we reached the end of hits or we already requested a new page.
     */
    public boolean hasMoreHits() {
        return !(endReached || lastRequestPage > lastResponsePage);
    }

    /**
     * Resets the helper's state.
     */
    @NonNull
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public Searcher reset() {
        lastResponsePage = 0;
        lastRequestPage = 0;
        lastResponseId = 0;
        endReached = false;
        clearFacetRefinements();
        cancelPendingRequests();
        numericRefinements.clear();
        return this;
    }

    /**
     * Checks if some requests are still waiting for a response.
     *
     * @return true if there is at least one pending request.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public boolean hasPendingRequests() {
        return pendingRequests.size() != 0;
    }

    /**
     * Cancels all requests still waiting for a response.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
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

    /**
     * Adds a facet refinement for the next queries.
     *
     * @param attributeName      the facet name.
     * @param isDisjunctiveFacet if {@code true}, the facet will be added as a disjunctive facet.
     * @param values             an eventual list of values to refine on.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public void addFacet(@NonNull String attributeName, boolean isDisjunctiveFacet, @Nullable ArrayList<String> values) {
        if (isDisjunctiveFacet) {
            disjunctiveFacets.add(attributeName);
        }
        if (values == null) {
            values = new ArrayList<>();
        }
        refinementMap.put(attributeName, values);
    }

    /**
     * Adds or removes this facet refinement for the next queries according to its enabled status.
     *
     * @param attributeName the attribute to facet on.
     * @param value         the value for this attribute.
     * @param active        if {@code true}, this facet value is currently refined on.
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
     * Adds a facet refinement for the next queries.
     * <p>
     * <b>This method resets the current page to 0.</b>
     *
     * @param attributeName the attribute to refine on.
     * @param value         the facet's value to refine with.
     */
    @NonNull
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public Searcher addFacetRefinement(@NonNull String attributeName, @NonNull String value) {
        List<String> attributeRefinements = getOrCreateRefinements(attributeName);
        attributeRefinements.add(value);
        rebuildQueryFacetFilters();
        return this;
    }

    /**
     * Removes a facet refinement for the next queries.
     * <p>
     * <b>This method resets the current page to 0.</b>
     *
     * @param attributeName the attribute to refine on.
     * @param value         the facet's value to refine with.
     */
    @NonNull
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public Searcher removeFacetRefinement(@NonNull String attributeName, @NonNull String value) {
        List<String> attributeRefinements = getOrCreateRefinements(attributeName);
        attributeRefinements.remove(value);
        rebuildQueryFacetFilters();
        return this;
    }

    /**
     * Checks if a facet refinement is enabled.
     *
     * @param attributeName the attribute to refine on.
     * @param value         the facet's value to check.
     * @return {@code true} if {@code attributeName} is being refined with {@code value}.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public boolean hasFacetRefinement(@NonNull String attributeName, @NonNull String value) {
        List<String> attributeRefinements = refinementMap.get(attributeName);
        return attributeRefinements != null && attributeRefinements.contains(value);
    }

    /**
     * Clears all facet refinements for the next queries.
     * <p>
     * <b>This method resets the current page to 0.</b>
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public Searcher clearFacetRefinements() {
        refinementMap.clear();
        disjunctiveFacets.clear();
        rebuildQueryFacetFilters();
        return this;
    }


    /**
     * Clears an attribute's facet refinements for the next queries.
     * <p>
     * <b>This method resets the current page to 0.</b>
     *
     * @param attribute the attribute's name.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public Searcher clearFacetRefinements(@NonNull String attribute) {
        final List<String> stringList = refinementMap.get(attribute);
        if (stringList != null) {
            stringList.clear();
        }
        disjunctiveFacets.remove(attribute);
        rebuildQueryFacetFilters();
        return this;
    }

    /**
     * Get the current numeric refinement for an attribute and an operator.
     *
     * @param attribute the attribute to refine on.
     * @param operator  one of the {@link NumericRefinement#OPERATOR_EQ operators} defined in {@link NumericRefinement}.
     * @return a {@link NumericRefinement} describing the current refinement for these parameters, or {@code null} if there is none.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public @Nullable
    NumericRefinement getNumericRefinement(@NonNull String attribute, int operator) {
        NumericRefinement.checkOperatorIsValid(operator);
        final SparseArray<NumericRefinement> attributeRefinements = numericRefinements.get(attribute);
        return attributeRefinements == null ? null : attributeRefinements.get(operator);
    }

    /**
     * Adds a numeric refinement for the next queries.
     *
     * @param refinement a {@link NumericRefinement} refining an attribute with a numerical value.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public Searcher addNumericRefinement(@NonNull NumericRefinement refinement) {
        bus.post(new NumericRefinementEvent(Operation.ADD, refinement));
        SparseArray<NumericRefinement> refinements = numericRefinements.get(refinement.attribute);
        if (refinements == null) {
            refinements = new SparseArray<>();
        }
        refinements.put(refinement.operator, refinement);
        numericRefinements.put(refinement.attribute, refinements);
        rebuildQueryNumericFilters();
        return this;
    }

    /**
     * Removes any numeric refinements relative to a specific attribute for the next queries.
     *
     * @param attribute the attribute that may have a refinement.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public Searcher removeNumericRefinement(@NonNull String attribute) {
        return removeNumericRefinement(attribute, NumericRefinement.OPERATOR_UNKNOWN, NumericRefinement.VALUE_UNKNOWN);
    }


    /**
     * Removes the given numeric refinement for the next queries.
     *
     * @param refinement a description of the refinement to remove.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public Searcher removeNumericRefinement(@NonNull NumericRefinement refinement) {
        return removeNumericRefinement(refinement.attribute, refinement.operator, refinement.value);
    }

    /**
     * Removes the numeric refinement relative to an attribute and operator for the next queries.
     *
     * @param attribute an attribute that maybe has some refinements.
     * @param operator  an {@link NumericRefinement#OPERATOR_EQ operator}.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public Searcher removeNumericRefinement(@NonNull String attribute, @NonNull Integer operator) {
        return removeNumericRefinement(attribute, operator, NumericRefinement.VALUE_UNKNOWN);
    }

    private Searcher removeNumericRefinement(@NonNull String attribute, @NonNull Integer operator, @NonNull Double value) {
        if (operator == NumericRefinement.OPERATOR_UNKNOWN) {
            numericRefinements.remove(attribute);
        } else {
            NumericRefinement.checkOperatorIsValid(operator);
            numericRefinements.get(attribute).remove(operator);
        }
        bus.post(new NumericRefinementEvent(Operation.REMOVE, new NumericRefinement(attribute, operator, value)));
        rebuildQueryNumericFilters();
        return this;
    }

    /**
     * Adds a boolean refinement for the next queries.
     *
     * @param attribute the attribute to refine on.
     * @param value     the value to refine with.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public Searcher addBooleanFilter(String attribute, Boolean value) {
        booleanFilterMap.put(attribute, value);
        rebuildQueryFacetFilters();
        return this;
    }

    /**
     * Gets the current boolean refinement for an attribute.
     *
     * @param attribute the attribute that may have a refinement.
     * @return the refinement value, or {@code null} if there is none.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public @Nullable Boolean getBooleanFilter(String attribute) {
        return booleanFilterMap.get(attribute);
    }

    /**
     * Removes any boolean refinement for an attribute for the next queries.
     *
     * @param attribute the attribute that may have a refinement.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public Searcher removeBooleanFilter(String attribute) {
        booleanFilterMap.remove(attribute);
        rebuildQueryFacetFilters();
        return this;
    }

    /**
     * Adds one or several attributes to facet on for the next queries.
     *
     * @param attributes one or more attribute names.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public Searcher addFacet(String... attributes) {
        for (String attribute : attributes) {
            final Integer value = facetRequestCount.get(attribute);
            facetRequestCount.put(attribute, value == null ? 1 : value + 1);
            if (value == null || value == 0) {
                facets.add(attribute);
            }
        }
        rebuildQueryFacets();
        return this;
    }

    /**
     * Removes one or several faceted attributes for the next queries.
     * If the facet was added several times, you need to call this method several times too or use {@link #deleteFacet}.
     *
     * @param attributes one or more attribute names.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public Searcher removeFacet(String... attributes) {
        for (String attribute : attributes) {
            final Integer value = facetRequestCount.get(attribute);
            if (value == null) {
                Log.e("Algolia|Searcher", "removeFacet called for" + attribute + " which was not currently a facet.");
            } else if (value == 1) {
                facets.remove(attribute);
                facetRequestCount.put(attribute, 0);
            } else {
                facetRequestCount.put(attribute, value - 1);
            }
        }
        rebuildQueryFacets();
        return this;
    }

    /**
     * Forces removal of one or several faceted attributes for the next queries.
     *
     * @param attributes one or more attribute names.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public Searcher deleteFacet(String... attributes) {
        for (String attribute : attributes) {
            facetRequestCount.put(attribute, 0);
            facets.remove(attribute);
        }
        rebuildQueryFacets();
        return this;
    }

    Searcher registerResultListener(@NonNull AlgoliaResultListener resultListener) {
        if (!resultListeners.contains(resultListener)) {
            resultListeners.add(resultListener);
        }
        return this;
    }

    Searcher registerErrorListener(@NonNull AlgoliaErrorListener errorListener) {
        if (!errorListeners.contains(errorListener)) {
            errorListeners.add(errorListener);
        }
        return this;
    }

    /**
     * Checks if a response's json contains at least one hit.
     *
     * @param jsonObject the JSONObject to check.
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

    /**
     * Gets the current {@link Searcher#query}.
     *
     * @return the Searcher's query.
     */
    public Query getQuery() {
        return query;
    }

    /**
     * Takes the given query's parameters for following search queries.
     * <p>
     * <b>This method resets the current page to 0.</b>
     *
     * @param query a {@link Query} object with some parameters set.
     */
    @NonNull
    public Searcher setQuery(@NonNull Query query) {
        this.query = query;
        this.query.setPage(0);
        return this;
    }


    /**
     * Gets the current {@link Searcher#index}.
     *
     * @return the Searcher's index.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public Index getIndex() {
        return index;
    }

    /**
     * Change the targeted index for future queries.
     * <p>
     * <b>Be aware that as index ordering may differ, this method will reset the current page to 0.</b>
     * You may want to use {@link Searcher#reset} to reinitialize the helper to an empty state.
     *
     * @param indexName name of the new index.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public @NonNull Searcher setIndex(@NonNull String indexName) {
        index = client.getIndex(indexName);
        query.setPage(0);
        return this;
    }

    @Deprecated //DISCUSS: Should we expose this?
    public int getId() {
        return id;
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
                updateFacetStat(facets, facets_stats, keys.next());
            }
        }
    }

    private void updateFacetStat(JSONObject facets, JSONObject facets_stats, String attribute) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double sum = 0;
        double avg;

        if (facets_stats != null) {
            JSONObject attributeStats = facets_stats.optJSONObject(attribute);
            if (attributeStats != null) { // Numerical attribute, let's use existing facets_stats
                try {
                    min = attributeStats.getDouble("min");
                    max = attributeStats.getDouble("max");
                    sum = attributeStats.getDouble("sum");
                    avg = attributeStats.getDouble("avg");
                    facetStats.put(attribute, new FacetStat(min, max, avg, sum));
                    return;
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

    @Deprecated //DISCUSS: Should we expose this?
    public
    @Nullable
    FacetStat getFacetStat(String attribute) {
        return facetStats.get(attribute);
    }

    /**
     * Update the facet stats, calling {@link Index#search(Query)} without notifying listeners of the result.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public void getUpdatedFacetStats() {
        index.searchAsync(query, new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject content, AlgoliaException error) {
                if (error == null) {
                    updateFacetStats(content);
                } else {
                    Log.e("Algolia|Searcher", "Error while getting updated facet stats:" + error.getMessage());
                }
            }
        });
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
        for (Map.Entry<String, Boolean> entry : booleanFilterMap.entrySet()) {
            facetFilters.put(entry.getKey() + ":" + entry.getValue());
        }
        //noinspection deprecation Deprecated for app developers
        query.setFacetFilters(facetFilters);
        query.setPage(0);
        return this;
    }

    private void rebuildQueryNumericFilters() {
        JSONArray numericFilters = new JSONArray();
        for (SparseArray<NumericRefinement> refinements : numericRefinements.values()) {
            for (int i = 0; i < refinements.size(); i++) {
                numericFilters.put(refinements.valueAt(i).toString());
            }
        }
        //noinspection deprecation (deprecated for end-users of API Client)
        query.setNumericFilters(numericFilters);
        query.setPage(0);
    }

    private Searcher rebuildQueryFacets() {
        final String[] facetArray = this.facets.toArray(new String[this.facets.size()]);
        query.setFacets(facetArray);
        return this;
    }

    @NonNull private List<String> getOrCreateRefinements(@NonNull String attributeName) {
        List<String> attributeRefinements = refinementMap.get(attributeName);
        if (attributeRefinements == null) {
            attributeRefinements = new ArrayList<>();
            refinementMap.put(attributeName, attributeRefinements);
        }
        return attributeRefinements;
    }

    private void cancelRequest(Request request, Integer requestSeqNumber) {
        if (!request.isCancelled()) {
            request.cancel();
            bus.post(new CancelEvent(request, requestSeqNumber));
            pendingRequests.delete(requestSeqNumber);
        } else {
            throw new IllegalStateException("cancelRequest was called on a request that was already canceled.");
        }
    }

    private void checkIfLastPage(@NonNull JSONObject content) {
        if (content.optInt("nbPages") == content.optInt("page") + 1) {
            endReached = true;
        }
    }

    private void updateListeners(@NonNull JSONObject hits, boolean isLoadingMore) {
        for (AlgoliaResultListener listener : resultListeners) {
            listener.onResults(new SearchResults(hits), isLoadingMore);
        }
    }

}
