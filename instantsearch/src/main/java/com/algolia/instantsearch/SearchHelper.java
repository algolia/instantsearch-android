package com.algolia.instantsearch;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.algolia.instantsearch.model.Errors;
import com.algolia.instantsearch.model.Facet;
import com.algolia.instantsearch.views.AlgoliaResultsView;
import com.algolia.instantsearch.views.Hits;
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

public class SearchHelper {

    private static int itemLayoutId;

    private Index index;
    private final Client client;
    private Query query;

    private SearchView searchView;
    private AlgoliaResultsView resultsView;
    private RefinementList refinementList;

    private int lastSearchSeqNumber; // Identifier of last fired query
    private int lastDisplayedSeqNumber; // Identifier of last displayed query
    private int lastRequestedPage;
    private int lastDisplayedPage;
    private boolean endReached;

    private Map<String, Pair<Integer, List<String>>> refinementMap = new HashMap<>();
    private List<Integer> pendingRequests = new ArrayList<>();
    private List<Integer> cancelledRequests = new ArrayList<>();

    /**
     * Create and initialize the helper.
     *
     * @param activity      the Activity containing an {@link AlgoliaResultsView}.
     * @param applicationId your application's ID.
     * @param apiKey        a search api key associated with this application.
     * @param indexName     the name of the application's index to search in.
     */
    public SearchHelper(final Activity activity, final String applicationId, final String apiKey, final String indexName) {
        this(applicationId, apiKey, indexName);

        processActivity(activity);
    }

    public SearchHelper(final AlgoliaResultsView resultsView, final String applicationId, final String apiKey, final String indexName) {
        this(applicationId, apiKey, indexName);

        this.resultsView = resultsView;
        resultsView.onInit(this);
    }

    private SearchHelper(String applicationId, String apiKey, String indexName) {
        client = new Client(applicationId, apiKey);
        index = client.initIndex(indexName);
        query = new Query();
    }

    /**
     * Find if a returned json contains at least one hit.
     *
     * @param jsonObject the query result.
     * @return {@code true} if it contains a hits array with at least one non null element.
     */
    public static boolean hasHits(@Nullable JSONObject jsonObject) {
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
     * Start a new search with the current query.
     */
    public SearchHelper search() {
        search(searchView.getQuery().toString());
        return this;
    }

    /**
     * Start a search if the given intent is an ACTION_SEARCH intent.
     *
     * @param intent an Intent that may contain a search query.
     */
    public SearchHelper search(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchView.setQuery(query, false);
            searchView.clearFocus();
            search(query);
        }
        return this;
    }

    /**
     * Start a search with the given text.
     *
     * @param queryString a String to search on the index.
     */
    public SearchHelper search(final String queryString) {
        endReached = false;
        lastRequestedPage = 0;
        lastDisplayedPage = -1;
        final int currentSearchSeqNumber = ++lastSearchSeqNumber;
        pendingRequests.add(currentSearchSeqNumber);

        query.setQuery(queryString);
        index.searchAsync(query, new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject content, AlgoliaException error) {
                pendingRequests.remove(Integer.valueOf(currentSearchSeqNumber));
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
                }

                lastDisplayedSeqNumber = currentSearchSeqNumber;
                lastDisplayedPage = 0;

                if (error != null) {
                    resultsView.onError(query, error);
                    Log.e("PLN|search.searchError", String.format("Index %s with query %s failed: %s(%s).", index.getIndexName(), queryString, error.getCause(), error.getMessage()));
                } else {
                    updateViews(content, false);
                    Log.d("PLN|search.searchResult", String.format("Index %s with query %s succeeded: %s.", index.getIndexName(), queryString, content));
                }
            }
        });
        return this;
    }

    /**
     * Load more results with the same query.
     */
    public SearchHelper loadMore() {
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
                        updateViews(content, true);
                        lastDisplayedPage = lastRequestedPage;
                    } else {
                        endReached = true;
                    }
                }
            }
        });
        return this;
    }

    /**
     * Tell if we should load more hits when reaching the end of an {@link AlgoliaResultsView}.
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
    public SearchHelper setBaseQuery(Query baseQuery) {
        query = baseQuery;
        return this;
    }

    /**
     * Change the targeted index for future queries.
     * Be aware that this method only changed the index without invalidating any existing state (pagination, facets, etc).
     * You may want to use {@link SearchHelper#reset} to reinitialize the helper to an empty state.
     *
     * @param indexName name of the new index.
     */
    public SearchHelper setIndex(String indexName) {
        index = client.initIndex(indexName);
        return this;
    }

    /**
     * Reset the helper's state.
     */
    public SearchHelper reset() {
        lastDisplayedPage = 0;
        lastRequestedPage = 0;
        lastDisplayedSeqNumber = 0;
        lastSearchSeqNumber = 0;
        endReached = false;
        updateViews(null, false);
        if (refinementList != null) {
            refinementList.reset();
        }
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

    private void processActivity(final Activity activity) {
        View rootView = activity.getWindow().getDecorView().getRootView();
        searchView = getSearchView(rootView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // Ignore submission events: we already update on changes.
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {
                    updateViews(null, false);
                } else {
                    search(newText);
                }
                return true;
            }
        });

        linkSearchViewToActivity(activity, searchView);

        resultsView = (AlgoliaResultsView) rootView.findViewById(R.id.hits);
        if (resultsView == null) {
            throw new IllegalStateException(Errors.LAYOUT_MISSING_HITS);
        }
        resultsView.onInit(this);

        if (resultsView instanceof Hits) {
            final Hits hits = (Hits) resultsView;
            query.setHitsPerPage(hits.getHitsPerPage());

            // Link hits to activity's empty view
            hits.setEmptyView(getEmptyView(rootView));

            final String layoutName = hits.getLayoutName();
            if (layoutName == null) {
                throw new IllegalStateException(Errors.LAYOUT_MISSING_HITS_ITEMLAYOUT);
            } else {
                itemLayoutId = activity.getResources().getIdentifier(layoutName, "layout", activity.getPackageName());
            }
        } else if (resultsView instanceof ListView) {
            ((ListView) resultsView).setEmptyView(getEmptyView(rootView));
        }

        refinementList = (RefinementList) rootView.findViewById(R.id.refinements);
        if (refinementList != null) {
            query.setFacets(refinementList.getAttributeName());
            refinementList.onInit(this);
            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    query.setFacetFilters(new JSONArray());
                    refinementList.reset();
                    return false;
                }
            });
        }
    }

    /**
     * Initialise search from the given activity according to its {@link android.app.SearchableInfo searchable info}.
     *
     * @param activity the activity with a {@link SearchView} identified as @+id/searchBox.
     */
    public static void initSearchFrom(Activity activity) {
        final View rootView = activity.getWindow().getDecorView().getRootView();
        SearchView searchView = getSearchView(rootView);

        linkSearchViewToActivity(activity, searchView);
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
    public void updateFacetRefinement(String attributeName, Facet facet) {
        if (facet.isEnabled()) {
            addFacetRefinement(attributeName, facet.getName());
        } else {
            removeFacetRefinement(attributeName, facet.getName());
        }
    }

    /**
     * Add a facet refinement and run again the current query.
     *
     * @param attribute the attribute to refine on.
     * @param value     the facet's value to refine with.
     */
    public void addFacetRefinement(String attribute, String value) {
        refinementMap.get(attribute).second.add(value);
        rebuildQueryFacetRefinements();
    }

    /**
     * Remove a facet refinement and run again the current query.
     *
     * @param attribute the attribute to refine on.
     * @param value     the facet's value to refine with.
     */
    public void removeFacetRefinement(String attribute, String value) {
        refinementMap.get(attribute).second.remove(value);
        rebuildQueryFacetRefinements();
    }

    /**
     * Checks if a facet refinement is enabled.
     *
     * @param attribute the attribute to refine on.
     * @param value     the facet's value to check.
     * @return {@code true} if {@code attribute} is being refined with {@code value}.
     */
    public boolean hasFacetRefinement(String attribute, String value) {
        return refinementMap.get(attribute).second.contains(value);
    }

    /**
     * Clears the facet refinements.
     *
     * @param attribute if not null, only this attribute's refinements will be cleared.
     */
    public void clearFacetRefinements(@Nullable String attribute) {
        if (attribute == null) {
            final Collection<Pair<Integer, List<String>>> values = refinementMap.values();
            for (Pair<Integer, List<String>> pair : values) {
                pair.second.clear();
            }
        } else {
            refinementMap.get(attribute).second.clear();
        }
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
        search();
    }

    private static void linkSearchViewToActivity(Activity activity, SearchView searchView) {
        SearchManager manager = (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(manager.getSearchableInfo(activity.getComponentName()));
    }

    private void updateViews(JSONObject hits, boolean isLoadingMore) {
        resultsView.onUpdateView(hits, isLoadingMore);
        if (refinementList != null) {
            refinementList.onUpdateView(hits, isLoadingMore);
        }
    }

    public static int getItemLayoutId() {
        if (itemLayoutId == 0) {
            throw new IllegalStateException(Errors.GET_ITEMLAYOUT_WITHOUT_HITS);
        }
        return itemLayoutId;
    }

    @NonNull
    private static SearchView getSearchView(View rootView) {
        try {
            SearchView searchView = (SearchView) rootView.findViewById(R.id.searchBox);
            if (searchView == null) {
                throw new IllegalStateException(Errors.LAYOUT_MISSING_SEARCHBOX);
            }
            return searchView;
        } catch (ClassCastException e) {
            throw new IllegalStateException(Errors.LAYOUT_MISSING_SEARCHBOX);
        }
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
}
