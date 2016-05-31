package com.algolia.instantsearch;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.algolia.instantsearch.model.Errors;
import com.algolia.instantsearch.views.AlgoliaResultsView;
import com.algolia.instantsearch.views.Hits;
import com.algolia.instantsearch.views.SearchBox;
import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AlgoliaHelper {

    private static final Map<Integer, String> attributes = new HashMap<>();
    private static int itemLayoutId;

    private final Index index;
    private final Client client;
    private final Query query;

    private SearchBox searchBox;
    private AlgoliaResultsView resultsView;

    private int lastSearchSeqNumber; // Identifier of last fired query
    private int lastDisplayedSeqNumber; // Identifier of last displayed query
    private int lastRequestedPage;
    private int lastDisplayedPage;
    private boolean endReached;

    /**
     * Create and initialize the helper
     *
     * @param activity      the Activity containing an {@link AlgoliaResultsView}
     * @param applicationId your application's ID
     * @param apiKey        a search api key associated with this application
     * @param indexName     the name of the application's index to search in
     */
    public AlgoliaHelper(Activity activity, final String applicationId, final String apiKey, final String indexName) {
        client = new Client(applicationId, apiKey);
        index = client.initIndex(indexName);
        query = new Query();

        processActivity(activity);
    }


    /**
     * Create and initialize the helper
     *
     * @param activity      the Activity containing an {@link AlgoliaResultsView}
     * @param applicationId your application's ID
     * @param apiKey        a search api key associated with this application
     * @param indexName     the name of the application's index to search in
     * @param baseQuery     a {@link Query} which properties will be applied to all search queries
     */
    public AlgoliaHelper(Activity activity, final String applicationId, final String apiKey, final String indexName, @NonNull Query baseQuery) {
        client = new Client(applicationId, apiKey);
        index = client.initIndex(indexName);
        query = new Query(baseQuery);

        processActivity(activity);
    }

    public static Set<Map.Entry<Integer, String>> getEntrySet() {
        return attributes.entrySet();
    }

    @SuppressWarnings("unused") // called via Data Binding
    @BindingAdapter({"attribute"})
    public static void bindAttribute(View view, String attributeName) {
        final int id = view.getId();
        if (notAlreadyMapped(id)) { // only map when you see a view for the first time.
            mapAttribute(attributeName, id);
        }
    }

    static boolean notAlreadyMapped(int id) {
        return attributes.get(id) == null;
    }

    private static void mapAttribute(String attributeName, int viewId) {
        attributes.put(viewId, attributeName);
    }

    public static int getItemLayoutId() {
        if (itemLayoutId == 0) {
            throw new IllegalStateException(Errors.GET_ITEMLAYOUT_WITHOUT_HITS);
        }
        return itemLayoutId;
    }

    /**
     * Find if a returned json contains at least one hit
     *
     * @param jsonObject the query result
     * @return true if it contains a hits array with at least one non null element
     */
    private static boolean hasHits(JSONObject jsonObject) {
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

    public void search(final String queryString, final CompletionHandler listener) {
        endReached = false;
        lastRequestedPage = 0;
        lastDisplayedPage = -1;
        final int currentSearchSeqNumber = ++lastSearchSeqNumber;

        query.setQuery(queryString);
        index.searchAsync(query, new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject content, AlgoliaException error) {
                // NOTE: Check that the received results are newer that the last displayed results.
                //
                // Rationale: Although TCP imposes a server to send responses in the same order as
                // requests, nothing prevents the system from opening multiple connections to the
                // same server, nor the Algolia client to transparently switch to another server
                // between two requests. Therefore the order of responses is not guaranteed.
                if (currentSearchSeqNumber <= lastDisplayedSeqNumber) {
                    return;
                }


                if (error == null) {
                    Log.d("PLN|search.searchResult", String.format("Index %s with query %s succeeded: %s.", index.getIndexName(), queryString, content));
                } else {
                    Log.e("PLN|search.searchError", String.format("Index %s with query %s failed: %s(%s).", index.getIndexName(), queryString, error.getCause(), error.getMessage()));
                }

                if (content == null || !hasHits(content)) {
                    endReached = true;
                }

                lastDisplayedSeqNumber = currentSearchSeqNumber;
                lastDisplayedPage = 0;

                listener.requestCompleted(content, error);
            }
        });
    }

    public void loadMore() {
        Query loadMoreQuery = new Query(query);
        loadMoreQuery.setPage(++lastRequestedPage);
        final int currentSearchSeqNumber = ++lastSearchSeqNumber;
        index.searchAsync(loadMoreQuery, new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject content, AlgoliaException error) {
                if (error != null) {
                    throw new RuntimeException(Errors.LOADMORE_FAIL, error);
                } else {
                    if (currentSearchSeqNumber <= lastDisplayedSeqNumber) {
                        return; // Hits are for an older query, let's ignore them
                    }

                    if (hasHits(content)) {
                        resultsView.onUpdateView(content, false);
                        lastDisplayedPage = lastRequestedPage;
                    } else {
                        endReached = true;
                    }
                }
            }
        });
    }

    private void processActivity(final Activity activity) {
        View rootView = activity.getWindow().getDecorView().getRootView();
        searchBox = (SearchBox) rootView.findViewById(R.id.searchBox);
        if (searchBox == null) {
            throw new IllegalStateException(Errors.LAYOUT_MISSING_SEARCHBOX);
        }

        resultsView = (AlgoliaResultsView) rootView.findViewById(R.id.hits);
        if (resultsView == null) {
            throw new IllegalStateException(Errors.LAYOUT_MISSING_HITS);
        }
        resultsView.onInit(this);

        if (resultsView instanceof Hits) {
            final Hits hits = (Hits) resultsView;
            query.setHitsPerPage(hits.getHitsPerPage());

            // Link hits to activity's empty view
            View emptyView = rootView.findViewById(R.id.empty);
            if (emptyView == null) {
                throw new IllegalStateException(Errors.LAYOUT_MISSING_EMPTY);
            }
            hits.setEmptyView(emptyView);
            final String layoutName = hits.getLayoutName();
            if (layoutName == null) {
                throw new IllegalStateException(Errors.LAYOUT_MISSING_HITS_ITEMLAYOUT);
            } else {
                itemLayoutId = activity.getResources().getIdentifier(layoutName, "layout", activity.getPackageName());
            }
        }

        // Link searchBox to the Activity's SearchableInfo
        SearchManager manager = (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);
        searchBox.setSearchableInfo(manager.getSearchableInfo(activity.getComponentName()));
    }

    /**
     * Tells if we should load more hits when reaching the end of an {@link AlgoliaResultsView}
     *
     * @return true unless we reached the end of hits or we already requested a new page
     */
    public boolean shouldLoadMore() {
        return !(endReached || lastRequestedPage > lastDisplayedPage);
    }
}
