package com.algolia.instantsearch;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

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
    private Query query;

    private SearchView searchView;
    private AlgoliaResultsView resultsView;

    private int lastSearchSeqNumber; // Identifier of last fired query
    private int lastDisplayedSeqNumber; // Identifier of last displayed query
    private int lastRequestedPage;
    private int lastDisplayedPage;
    private boolean endReached;

    /**
     * Create and initialize the helper.
     *
     * @param activity      the Activity containing an {@link AlgoliaResultsView}.
     * @param applicationId your application's ID.
     * @param apiKey        a search api key associated with this application.
     * @param indexName     the name of the application's index to search in.
     */
    public AlgoliaHelper(Activity activity, final String applicationId, final String apiKey, final String indexName) {
        client = new Client(applicationId, apiKey);
        index = client.initIndex(indexName);
        query = new Query();

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

    public static int getItemLayoutId() {
        if (itemLayoutId == 0) {
            throw new IllegalStateException(Errors.GET_ITEMLAYOUT_WITHOUT_HITS);
        }
        return itemLayoutId;
    }

    private static void mapAttribute(String attributeName, int viewId) {
        attributes.put(viewId, attributeName);
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

    /**
     * Start a search if the given intent is an ACTION_SEARCH intent.
     *
     * @param intent an Intent that may contain a search query.
     */
    public void search(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            search(query);
        }
    }

    /**
     * Start a search with the given text.
     *
     * @param queryString a String to search on the index.
     */
    public void search(final String queryString) {
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

                if (content == null || !hasHits(content)) {
                    endReached = true;
                }

                lastDisplayedSeqNumber = currentSearchSeqNumber;
                lastDisplayedPage = 0;

                if (error != null) {
                    resultsView.onError(query, error);
                    Log.e("PLN|search.searchError", String.format("Index %s with query %s failed: %s(%s).", index.getIndexName(), queryString, error.getCause(), error.getMessage()));
                } else {
                    resultsView.onUpdateView(content, true);
                    Log.d("PLN|search.searchResult", String.format("Index %s with query %s succeeded: %s.", index.getIndexName(), queryString, content));
                }
            }
        });
    }

    /**
     * Load more results with the same query.
     */
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

    /**
     * Tell if we should load more hits when reaching the end of an {@link AlgoliaResultsView}
     *
     * @return true unless we reached the end of hits or we already requested a new page
     */
    public boolean shouldLoadMore() {
        return !(endReached || lastRequestedPage > lastDisplayedPage);
    }

    /**
     * Use the given query's parameters for following search queries
     *
     * @param baseQuery a {@link Query} object with some parameters set
     */
    public void setBaseQuery(Query baseQuery) {
        query = baseQuery;
    }

    private void processActivity(final Activity activity) {
        View rootView = activity.getWindow().getDecorView().getRootView();
        searchView = (SearchBox) rootView.findViewById(R.id.searchBox);
        if (searchView == null) {
            throw new IllegalStateException(Errors.LAYOUT_MISSING_SEARCHBOX);
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // Ignore submission events: we already update on changes.
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {
                    resultsView.onUpdateView(null, true);
                } else {
                    search(newText);
                }
                return true;
            }
        });

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

        // Link searchView to the Activity's SearchableInfo
        SearchManager manager = (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(manager.getSearchableInfo(activity.getComponentName()));
    }

    /**
     * Find the empty view in the given rootView.
     *
     * @param rootView the topmost view in the view hierarchy of the Activity.
     * @return the empty view if it was in the given rootView.
     * @throws IllegalStateException if no empty view can be found.
     */
    @NonNull
    private View getEmptyView(View rootView) {
        View emptyView = rootView.findViewById(R.id.empty);
        if (emptyView == null) {
            throw new IllegalStateException(Errors.LAYOUT_MISSING_EMPTY);
        }
        return emptyView;
    }
}
