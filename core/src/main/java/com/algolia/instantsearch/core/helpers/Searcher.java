package com.algolia.instantsearch.core.helpers;

import android.app.SearchManager;
import android.content.Intent;
import android.util.Log;
import android.util.SparseArray;

import com.algolia.instantsearch.core.BuildConfig;
import com.algolia.instantsearch.core.events.CancelEvent;
import com.algolia.instantsearch.core.events.ErrorEvent;
import com.algolia.instantsearch.core.events.FacetRefinementEvent;
import com.algolia.instantsearch.core.events.NumericRefinementEvent;
import com.algolia.instantsearch.core.events.QueryTextChangeEvent;
import com.algolia.instantsearch.core.events.RefinementEvent.Operation;
import com.algolia.instantsearch.core.events.ResultEvent;
import com.algolia.instantsearch.core.events.SearchEvent;
import com.algolia.instantsearch.core.model.AlgoliaErrorListener;
import com.algolia.instantsearch.core.model.AlgoliaResultsListener;
import com.algolia.instantsearch.core.model.Errors;
import com.algolia.instantsearch.core.model.FacetStat;
import com.algolia.instantsearch.core.model.NumericRefinement;
import com.algolia.instantsearch.core.model.SearchResults;
import com.algolia.search.saas.AbstractClient.LibraryVersion;
import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.algolia.search.saas.Request;
import com.algolia.search.saas.RequestOptions;
import com.algolia.search.saas.Searchable;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.algolia.instantsearch.core.events.RefinementEvent.Operation.ADD;
import static com.algolia.instantsearch.core.events.RefinementEvent.Operation.REMOVE;
import static com.algolia.instantsearch.core.events.ResultEvent.REQUEST_UNKNOWN;

/**
 * Handles the state of the search interface, wrapping an {@link Client Algolia API Client} and provide a level of abstraction over it.
 * <p>
 * The Searcher is responsible of interacting with the Algolia engine: when {@link Searcher#search()} is called,
 * the Searcher will fire a request with the current {@link Searcher#query}, and will forward the search results
 * (or error) to its {@link AlgoliaResultsListener result listeners} (or {@link AlgoliaErrorListener error listeners}).
 */
@SuppressWarnings("UnusedReturnValue") // chaining
public class Searcher {
    private static Map<String, Searcher> instances = new HashMap<>();

    /**
     * The {@link Searchable} targeted by this Searcher.
     */
    private Searchable searchable;

    /**
     * The {@link Client API Client} used by this Searcher.
     */
    private final Client client;
    /**
     * The current state of the search {@link Query}.
     */
    private Query query;
    /**
     * The variant identifying this Searcher.
     */
    @SuppressWarnings("WeakerAccess") // For library users
    public final String variant;

    /**
     * The {@link AlgoliaResultsListener listeners} that will receive search results.
     */
    private final List<AlgoliaResultsListener> resultListeners = new ArrayList<>();

    /**
     * The {@link AlgoliaErrorListener listeners} that will receive search results.
     */
    private final List<AlgoliaErrorListener> errorListeners = new ArrayList<>();

    /**
     * The identifier of the last search request fired by this Searcher.
     */
    private static int lastRequestId;
    /**
     * The identifier of the last search response propagated by this Searcher.
     */
    private int lastResponseId; // Identifier of last displayed query
    /**
     * The page number of the last search request fired by this Searcher.
     */
    private int lastRequestPage;
    /**
     * The page number of the last search response propagated by this Searcher.
     */
    private int lastResponsePage;

    /**
     * Whether the end of the results has been reached for the current {@link Searcher#query}.
     */
    private boolean endReached;

    /**
     * The List of attributes that will be treated as disjunctive facets.
     */
    private final List<String> disjunctiveFacets = new ArrayList<>();
    /**
     * The Map associating attributes with their respective refinement value(s).
     */
    private final Map<String, List<String>> refinementMap = new HashMap<>();
    /**
     * The Map associating attributes with their respective numeric refinement value(s).
     */
    private final Map<String, SparseArray<NumericRefinement>> numericRefinements = new HashMap<>();
    /**
     * The Map associating attributes with their respective boolean refinement value(s).
     */
    private final Map<String, Boolean> booleanFilterMap = new HashMap<>();

    /**
     * The List of attributes that will be used for faceting.
     */
    private final List<String> facets = new ArrayList<>();
    /**
     * A Map of FacetStats updated with every response.
     */
    private final Map<String, FacetStat> facetStats = new HashMap<>();
    /**
     * A Map to keep counts of facet additions, so we don't remove them unless we get as much removals.
     */
    private final HashMap<String, Integer> facetRequestCount = new HashMap<>();

    /**
     * The SparseArray associating pending requests with their {@link Searcher#lastRequestId identifier}.
     */
    private final SparseArray<Request> pendingRequests = new SparseArray<>();

    /**
     * Gets the default Searcher.
     *
     * @return the only (or alternatively the first) Searcher instance.
     * @throws IllegalStateException if no searcher was {@link #create(Searchable) created} before.
     */
    public static Searcher get() {
        return instances.get(instances.keySet().iterator().next());
    }

    /**
     * Gets the Searcher for a given variant.
     *
     * @param variant an identifier to differentiate this Searcher from eventual others.
     * @return the corresponding Searcher instance.
     * @throws IllegalStateException if no searcher was {@link #create(Searchable) created} before for this {@code variant}.
     */
    public static Searcher get(String variant) {
        final Searcher searcher = instances.get(variant);
        if (searcher == null) {
            throw new IllegalStateException(Errors.SEARCHER_GET_BEFORE_CREATE);
        }
        return searcher;
    }

    /**
     * Constructs a Searcher, creating its {@link Searcher#searchable} and {@link Searcher#client} with the given parameters.
     *
     * @param appId     your Algolia Application ID.
     * @param apiKey    a search-only API Key. (never use API keys that could modify your records! see https://www.algolia.com/doc/guides/security/api-keys)
     * @param indexName the name of the searchable to target.
     * @return the new instance.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public static Searcher create(@NonNull final String appId, @NonNull final String apiKey, @NonNull final String indexName) {
        return create(new Client(appId, apiKey).getIndex(indexName), indexName);
    }

    /**
     * Constructs a Searcher, creating its {@link Searcher#searchable} and {@link Searcher#client} with the given parameters.
     *
     * @param appId     your Algolia Application ID.
     * @param apiKey    a search-only API Key. (never use API keys that could modify your records! see https://www.algolia.com/doc/guides/security/api-keys)
     * @param indexName the name of the searchable to target.
     * @param variant   an identifier to differentiate this Searcher from eventual others.
     * @return the new instance.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public static Searcher create(@NonNull final String appId, @NonNull final String apiKey, @NonNull final String indexName, @NonNull String variant) {
        return create(new Client(appId, apiKey).getIndex(indexName), variant);
    }

    /**
     * Constructs a Searcher from an existing {@link Searchable}.
     *
     * @param searchable an Index initialized and eventually configured.
     * @return the new instance.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public static Searcher create(@NonNull final Searchable searchable) {
        final String key = searchable.toString();
        Searcher instance = instances.get(key);
        if (instance == null) {
            instance = new Searcher(searchable, key);
            instances.put(key, instance);
        } else {
            throw new IllegalStateException("There is already a Searcher for index " + key + ", you must specify a variant.");
        }
        return instance;
    }

    /**
     * Constructs the Searcher from an existing {@link Index}, eventually replacing the existing searcher for {@code variant}.
     *
     * @param variant an identifier to differentiate this Searcher from eventual others using the same index. Defaults to the index's name.
     * @param index   an Index initialized and eventually configured.
     * @return the new instance.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public static Searcher create(@NonNull final Searchable index, @NonNull String variant) {
        Searcher instance = instances.get(variant);
        if (instance == null || instance.getSearchable() != index) {
            instance = new Searcher(index, variant);
            instances.put(variant, instance);
        }
        return instance;
    }

    private Searcher(@NonNull final Searchable searchable, @NonNull String variant) {
        this.searchable = searchable;
        this.variant = variant;
        query = new Query();
        if (searchable instanceof Index) {
            this.client = ((Index) searchable).getClient();
            final LibraryVersion version = new LibraryVersion("InstantSearch Android", String.valueOf(BuildConfig.VERSION_NAME));
            if (!Arrays.asList(client.getUserAgents()).contains(version)) {
                client.addUserAgent(version);
            }
        } else {
            this.client = null;
        }
    }

    /**
     * Destroys all Searcher instances.
     */
    public static void destroyAll() {
        instances.clear();
    }

    /**
     * Starts a search with the current helper's state.
     *
     * @return this {@link Searcher} for chaining.
     */
    @NonNull
    public Searcher search() {
        return search(null, null);
    }

    /**
     * Starts a search with the given text.
     *
     * @param queryString a String to search on the index.
     * @return this {@link Searcher} for chaining.
     */
    @NonNull
    public Searcher search(@Nullable final String queryString) {
        return search(queryString, null);
    }

    /**
     * Starts a search, with the given intent's query if it is a {@link Intent#ACTION_SEARCH search intent}.
     *
     * @param intent an Intent that could contain a {@link SearchManager#QUERY QUERY}.
     * @return this {@link Searcher} for chaining.
     */
    @NonNull
    public Searcher search(@Nullable final Intent intent) {
        String query = null;
        Object origin = null;
        if (intent != null) {
            if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
                query = intent.hasExtra(SearchManager.QUERY) ?
                        intent.getStringExtra(SearchManager.QUERY) : intent.getDataString();
                origin = intent;
            }
        }
        return search(query, origin);
    }

    /**
     * Starts a search with the given text from the given origin.
     *
     * @param queryString a String to search on the index.
     * @param origin      an optional origin to identify the change.
     * @return this {@link Searcher} for chaining.
     */
    @NonNull
    public Searcher search(@Nullable final String queryString, @Nullable final Object origin) {
        if (queryString != null) {
            query.setQuery(queryString);
            EventBus.getDefault().post(new QueryTextChangeEvent(queryString, origin == null ? this : origin));
        }
        endReached = false;
        lastRequestPage = 0;
        lastResponsePage = -1;
        final int currentRequestId = ++lastRequestId;

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
                    postError(error, currentRequestId);
                } else {
                    if (content == null) {
                        Log.e("Algolia|Searcher", "content is null but error too.");
                    } else {
                        EventBus.getDefault().post(new ResultEvent(Searcher.this, content, query, currentRequestId));
                        updateListeners(content, false);
                        updateFacetStats(content);
                    }
                }
            }
        };

        final Request searchRequest;
        searchRequest = triggerSearch(searchHandler);
        EventBus.getDefault().post(new SearchEvent(this, query, currentRequestId));
        pendingRequests.put(currentRequestId, searchRequest);
        return this;
    }

    /**
     * Forwards the given algolia response to the {@link Searcher#resultListeners results listeners}.
     * <p>
     * <i>This method is useful if you rely on a backend implementation,
     * but still want to use InstantSearch Android in your frontend application.</i>
     *
     * @param response the response sent by the algolia server.
     * @return this {@link Searcher} for chaining.
     * @throws IllegalStateException if the given response is malformated.
     */
    @NonNull
    public Searcher forwardBackendSearchResult(@NonNull JSONObject response) {
        if (!hasHits(response)) {
            endReached = true;
        } else {
            checkIfLastPage(response);
        }

        updateListeners(response, false);
        updateFacetStats(response);
        EventBus.getDefault().post(new ResultEvent(this, response, query, REQUEST_UNKNOWN));
        return this;
    }

    /**
     * Loads more results with the same query.
     * <p>
     * Note that this method won't do anything if {@link Searcher#hasMoreHits} returns false.
     *
     * @return this {@link Searcher} for chaining.
     */
    @NonNull
    public Searcher loadMore() {
        if (!hasMoreHits()) {
            return this;
        }
        query.setPage(++lastRequestPage);
        final int currentRequestId = ++lastRequestId;
        EventBus.getDefault().post(new SearchEvent(this, query, currentRequestId));
        pendingRequests.put(currentRequestId, triggerSearch(new CompletionHandler() {
            @Override
            public void requestCompleted(@NonNull JSONObject content, @Nullable AlgoliaException error) {
                pendingRequests.remove(currentRequestId);
                if (error != null) {
                    postError(error, currentRequestId);
                } else {
                    if (currentRequestId <= lastResponseId) {
                        return; // Hits are for an older query, let's ignore them
                    }

                    if (hasHits(content)) {
                        updateListeners(content, true);
                        updateFacetStats(content);
                        lastResponsePage = lastRequestPage;

                        checkIfLastPage(content);
                    } else {
                        endReached = true;
                    }
                    EventBus.getDefault().post(new ResultEvent(Searcher.this, content, query, currentRequestId));
                }
            }
        }));
        return this;
    }

    private Request triggerSearch(CompletionHandler searchHandler) {
        Request searchRequest;
        if (!disjunctiveFacets.isEmpty()) {
            searchRequest = searchable.searchDisjunctiveFacetingAsync(query, disjunctiveFacets, refinementMap, null, searchHandler);
        } else {
            searchRequest = searchable.searchAsync(query, searchHandler);
        }
        return searchRequest;
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
     *
     * @return this {@link Searcher} for chaining.
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
     *
     * @return this {@link Searcher} for chaining.
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

    //region Facet Refinements

    /**
     * Adds a facet refinement for the next queries.
     *
     * @param attribute     the facet name.
     * @param isDisjunctive if {@code true}, the facet will be added as a disjunctive facet.
     * @param values        an eventual list of values to refine on.
     * @deprecated this will be removed in v2.0, use {@link #addFacetRefinement(String, List, boolean)} instead.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    @Deprecated // TODO Remove in 2.0
    public void addFacet(@NonNull String attribute, boolean isDisjunctive, @Nullable List<String> values) {
        addFacetRefinement(attribute, values, isDisjunctive);
    }

    /**
     * Sets a given facet as disjunctive or conjunctive.
     *
     * @param attribute     The facet's name.
     * @param isDisjunctive {@code true} to treat this facet as disjunctive (`OR`), false to treat it as conjunctive (`AND`, the default).
     * @return this {@link Searcher} for chaining.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public Searcher setFacet(@NonNull String attribute, boolean isDisjunctive) {
        if (isDisjunctive && !disjunctiveFacets.contains(attribute)) {
            disjunctiveFacets.add(attribute);
        } else {
            disjunctiveFacets.remove(attribute);
        }
        return this;
    }

    /**
     * Adds a facet refinement for the next queries.
     * <p>
     * <b>This method resets the current page to 0.</b>
     *
     * @param attribute the attribute to refine on.
     * @param value     the facet's value to refine with.
     * @return this {@link Searcher} for chaining.
     */
    @NonNull
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public Searcher addFacetRefinement(@NonNull String attribute, @NonNull String value) {
        return addFacetRefinement(attribute, Collections.singletonList(value), disjunctiveFacets.contains(attribute));
    }

    /**
     * Adds a facet refinement for the next queries.
     * <p>
     * <b>This method resets the current page to 0.</b>
     *
     * @param attribute     the facet name.
     * @param values        an eventual list of values to refine on.
     * @param isDisjunctive if {@code true}, the facet will be added as a disjunctive facet.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public Searcher addFacetRefinement(@NonNull String attribute, @Nullable List<String> values, boolean isDisjunctive) {
        if (values == null) {
            values = new ArrayList<>();
        }
        if (isDisjunctive) {
            disjunctiveFacets.add(attribute);
        }
        List<String> attributeRefinements = getOrCreateRefinements(attribute);
        attributeRefinements.addAll(values);
        rebuildQueryFacetFilters();
        for (String value : values) {
            EventBus.getDefault().post(new FacetRefinementEvent(this, ADD, attribute, value, isDisjunctive));
        }
        return this;
    }


    /**
     * Removes a facet refinement for the next queries.
     * <p>
     * <b>This method resets the current page to 0.</b>
     *
     * @param attribute the attribute to refine on.
     * @param value     the facet's value to refine with.
     * @return this {@link Searcher} for chaining.
     */
    @NonNull
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public Searcher removeFacetRefinement(@NonNull String attribute, @NonNull String value) {
        List<String> attributeRefinements = getOrCreateRefinements(attribute);
        attributeRefinements.remove(value);
        rebuildQueryFacetFilters();
        EventBus.getDefault().post(new FacetRefinementEvent(this, REMOVE, attribute, value, disjunctiveFacets.contains(attribute)));
        return this;
    }

    /**
     * Adds or removes this facet refinement for the next queries according to its enabled status.
     *
     * @param attribute the attribute to facet on.
     * @param value     the value for this attribute.
     * @param active    if {@code true}, this facet value is currently refined on.
     * @return this {@link Searcher} for chaining.
     */
    @NonNull
    public Searcher updateFacetRefinement(@NonNull String attribute, @NonNull String value, boolean active) {
        if (active) {
            addFacetRefinement(attribute, value);
        } else {
            removeFacetRefinement(attribute, value);
        }
        return this;
    }

    /**
     * Checks if a facet refinement is enabled.
     *
     * @param attribute the attribute to refine on.
     * @param value     the facet's value to check.
     * @return {@code true} if {@code attribute} is being refined with {@code value}.
     */
    @SuppressWarnings({"WeakerAccess", "unused", "SameParameterValue"}) // For library users
    public boolean hasFacetRefinement(@NonNull String attribute, @NonNull String value) {
        List<String> attributeRefinements = refinementMap.get(attribute);
        return attributeRefinements != null && attributeRefinements.contains(value);
    }

    /**
     * Gets the refinement values for a faceted attribute.
     *
     * @param attribute the attribute refined on.
     * @return the refinements enabled for the given attribute, or {@code null} if there is none.
     */
    @SuppressWarnings({"WeakerAccess", "unused", "SameParameterValue"}) // For library users
    @Nullable
    public List<String> getFacetRefinements(@NonNull String attribute) {
        return refinementMap.get(attribute);
    }

    /**
     * Clears all facet refinements for the next queries.
     * <p>
     * <b>This method resets the current page to 0.</b>
     *
     * @return this {@link Searcher} for chaining.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public Searcher clearFacetRefinements() {
        refinementMap.clear();
        rebuildQueryFacetFilters();
        return this;
    }


    /**
     * Clears an attribute's facet refinements for the next queries.
     * <p>
     * <b>This method resets the current page to 0.</b>
     *
     * @param attribute the attribute's name.
     * @return this {@link Searcher} for chaining.
     */
    @SuppressWarnings({"WeakerAccess", "unused", "SameParameterValue"}) // For library users
    public Searcher clearFacetRefinements(@NonNull String attribute) {
        final List<String> stringList = refinementMap.get(attribute);
        if (stringList != null) {
            stringList.clear();
        }
        rebuildQueryFacetFilters();
        return this;
    }
    //endregion

    //region Numeric Refinements

    /**
     * Gets the current numeric refinement for an attribute and an operator.
     *
     * @param attribute the attribute to refine on.
     * @param operator  one of the {@link NumericRefinement#OPERATOR_EQ operators} defined in {@link NumericRefinement}.
     * @return a {@link NumericRefinement} describing the current refinement for these parameters, or {@code null} if there is none.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    @Nullable
    public NumericRefinement getNumericRefinement(@NonNull String attribute, int operator) {
        NumericRefinement.checkOperatorIsValid(operator);
        final SparseArray<NumericRefinement> attributeRefinements = numericRefinements.get(attribute);
        return attributeRefinements == null ? null : attributeRefinements.get(operator);
    }

    /**
     * Adds a numeric refinement for the next queries.
     *
     * @param refinement a {@link NumericRefinement} refining an attribute with a numerical value.
     * @return this {@link Searcher} for chaining.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public Searcher addNumericRefinement(@NonNull NumericRefinement refinement) {
        SparseArray<NumericRefinement> refinements = numericRefinements.get(refinement.attribute);
        if (refinements == null) {
            refinements = new SparseArray<>();
        }
        refinements.put(refinement.operator, refinement);
        numericRefinements.put(refinement.attribute, refinements);
        rebuildQueryNumericFilters();
        EventBus.getDefault().post(new NumericRefinementEvent(this, ADD, refinement));
        return this;
    }

    /**
     * Removes any numeric refinements relative to a specific attribute for the next queries.
     *
     * @param attribute the attribute that may have a refinement.
     * @return this {@link Searcher} for chaining.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public Searcher removeNumericRefinement(@NonNull String attribute) {
        return removeNumericRefinement(new NumericRefinement(attribute, NumericRefinement.OPERATOR_UNKNOWN, NumericRefinement.VALUE_UNKNOWN));
    }


    /**
     * Removes the numeric refinement relative to an attribute and operator for the next queries.
     *
     * @param attribute an attribute that maybe has some refinements.
     * @param operator  an {@link NumericRefinement#OPERATOR_EQ operator}.
     * @return this {@link Searcher} for chaining.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public Searcher removeNumericRefinement(@NonNull String attribute, @NonNull Integer operator) {
        return removeNumericRefinement(new NumericRefinement(attribute, operator, NumericRefinement.VALUE_UNKNOWN));
    }

    /**
     * Removes the given numeric refinement for the next queries.
     *
     * @param refinement a description of the refinement to remove.
     * @return this {@link Searcher} for chaining.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public Searcher removeNumericRefinement(@NonNull NumericRefinement refinement) {
        if (refinement.operator == NumericRefinement.OPERATOR_UNKNOWN) {
            numericRefinements.remove(refinement.attribute);
        } else {
            NumericRefinement.checkOperatorIsValid(refinement.operator);
            numericRefinements.get(refinement.attribute).remove(refinement.operator);
        }
        rebuildQueryNumericFilters();
        EventBus.getDefault().post(new NumericRefinementEvent(this, Operation.REMOVE, refinement));
        return this;
    }
    //endregion

    //region Boolean Refinements

    /**
     * Adds a boolean refinement for the next queries.
     *
     * @param attribute the attribute to refine on.
     * @param value     the value to refine with.
     * @return this {@link Searcher} for chaining.
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
    @Nullable
    public Boolean getBooleanFilter(String attribute) {
        return booleanFilterMap.get(attribute);
    }

    /**
     * Removes any boolean refinement for an attribute for the next queries.
     *
     * @param attribute the attribute that may have a refinement.
     * @return this {@link Searcher} for chaining.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public Searcher removeBooleanFilter(String attribute) {
        booleanFilterMap.remove(attribute);
        rebuildQueryFacetFilters();
        return this;
    }
    //endregion

    //region Facets

    /**
     * Adds one or several attributes to facet on for the next queries.
     *
     * @param attributes one or more attribute names.
     * @return this {@link Searcher} for chaining.
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
     * @return this {@link Searcher} for chaining.
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
     * @return this {@link Searcher} for chaining.
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
    //endregion

    /**
     * Links the given listener to the Searcher, which will forward new search results to it.
     *
     * @param resultListener an object implementing {@link AlgoliaResultsListener}.
     */
    public Searcher registerResultListener(@NonNull AlgoliaResultsListener resultListener) {
        if (!resultListeners.contains(resultListener)) {
            resultListeners.add(resultListener);
        }
        return this;
    }

    /**
     * Links the given listener to the Searcher, which will forward new search errors to it.
     *
     * @param errorListener an object implementing {@link AlgoliaErrorListener}.
     */
    public Searcher registerErrorListener(@NonNull AlgoliaErrorListener errorListener) {
        if (!errorListeners.contains(errorListener)) {
            errorListeners.add(errorListener);
        }
        return this;
    }

    /**
     * Unlinks the given listener to the Searcher, which will stop forwarding new search results to it.
     *
     * @param resultListener the {@link AlgoliaResultsListener result listener} to be removed.
     */
    public void unregisterResultListener(@NonNull AlgoliaResultsListener resultListener) {
        resultListeners.remove(resultListener);
    }

    /**
     * Unlinks the given listener to the Searcher, which will stop forwarding new search errors to it.
     *
     * @param errorListener the {@link AlgoliaErrorListener error listener} to be removed.
     */
    public void unregisterErrorListener(@NonNull AlgoliaErrorListener errorListener) {
        errorListeners.remove(errorListener);
    }

    //TODO REORGANIZE

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
     * @return this {@link Searcher} for chaining.
     */
    @NonNull
    public Searcher setQuery(@NonNull Query query) {
        this.query = query;
        this.query.setPage(0);
        return this;
    }


    /**
     * Gets the current {@link Searcher#searchable}.
     *
     * @return the Searcher's index.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public Searchable getSearchable() {
        return searchable;
    }

    /**
     * Gets the current {@link Searcher#searchable} as an Index.
     *
     * @return the Searcher's index.
     * @throws IllegalStateException if {@link Searcher#searchable} if not an instance of {@link Index}
     * @deprecated use {@link #getSearchable()} to get the Searcher's {@link #searchable} regardless of its implementation.
     */
    @Deprecated
    public Index getIndex() {
        if (searchable instanceof Index) {
            return (Index) searchable;
        }
        throw new IllegalStateException("This method requires an Algolia Index and not a custom Searchable");
    }

    /**
     * Changes the targeted index for future queries.
     * <p>
     * <b>Be aware that as index ordering may differ, this method will reset the current page to 0.</b>
     * You may want to use {@link Searcher#reset} to reinitialize the helper to an empty state.
     *
     * @param indexName name of the new index.
     * @return this {@link Searcher} for chaining.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    @NonNull
    public Searcher setIndex(@NonNull String indexName) {
        if (client == null) {
            throw new IllegalStateException("This method requires an Algolia Index and not a custom Searchable");
        }
        searchable = client.getIndex(indexName);
        query.setPage(0);
        return this;
    }


    /**
     * Unregisters and cleans up the searcher when is no longer needed.
     */
    public void destroy() {
        errorListeners.clear();
        resultListeners.clear();
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

    /**
     * Gets the statistics associated with a given facet.
     *
     * @param attribute the facet's name.
     * @return an object describing the min, max, average and sum of the facet's values.
     */
    public
    @Nullable
    FacetStat getFacetStat(String attribute) {
        return facetStats.get(attribute);
    }

    /**
     * Updates the facet stats, calling {@link Index#search(Query, RequestOptions)} without notifying listeners of the result.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public void getUpdatedFacetStats() {
        searchable.searchAsync(query, new CompletionHandler() {
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

    @Override
    public String toString() {
        String key = null;
        for (Map.Entry<String, Searcher> entry : instances.entrySet()) { //TODO: Refact with BiMap
            if (this.equals(entry.getValue())) {
                key = entry.getKey();
                break;
            }
        }
        return "Searcher{" + (key != null ? key : "") + "}";
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

    @NonNull
    private List<String> getOrCreateRefinements(@NonNull String attribute) {
        List<String> attributeRefinements = refinementMap.get(attribute);
        if (attributeRefinements == null) {
            attributeRefinements = new ArrayList<>();
            refinementMap.put(attribute, attributeRefinements);
        }
        return attributeRefinements;
    }

    private void cancelRequest(Request request, int requestSeqNumber) {
        if (!request.isCancelled()) {
            request.cancel();
            pendingRequests.delete(requestSeqNumber);
            EventBus.getDefault().post(new CancelEvent(this, request, requestSeqNumber));
        } else {
            throw new IllegalStateException("cancelRequest was called on a request that was already canceled.");
        }
    }

    private void checkIfLastPage(@NonNull JSONObject content) {
        if (content.optInt("nbPages") == content.optInt("page") + 1) {
            endReached = true;
        }
    }

    private void updateListeners(@NonNull JSONObject content, boolean isLoadingMore) {
        for (AlgoliaResultsListener listener : resultListeners) {
            listener.onResults(new SearchResults(content), isLoadingMore);
        }
    }

    private void postError(@NonNull AlgoliaException error, int currentRequestId) {
        for (AlgoliaErrorListener listener : errorListeners) {
            listener.onError(query, error);
        }
        EventBus.getDefault().post(new ErrorEvent(this, error, query, currentRequestId));
    }

}
