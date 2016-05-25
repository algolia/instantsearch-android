package com.algolia.instantsearch;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.util.Log;
import android.view.View;

import com.algolia.instantsearch.model.Errors;
import com.algolia.instantsearch.model.Result;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AlgoliaHelper {

    private static final Map<Integer, String> attributes = new HashMap<>();
    private static int itemLayoutId;

    private final Index index;
    private final Client client;
    private final Query query;
    private SearchBox searchBox;
    private Hits hits;

    private int lastSearchSeqNumber; // Identifier of last fired query
    private int lastDisplayedSeqNumber; // Identifier of last displayed query
    private int lastRequestedPage;
    private int lastDisplayedPage;
    private boolean endReached;

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

    private static void mapAttribute(String attributeName, int viewId) {
        attributes.put(viewId, attributeName);
    }

    public static int getItemLayoutId() {
        return itemLayoutId;
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

                //TODO: Avoid useless parse -> refactor listener with Results instead of JSONO?
                final List<Result> results = parseResults(content);
                if (results == null || results.isEmpty()) {
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
                        return; // Results are for an older query, let's ignore them
                    }

                    List<Result> results = parseResults(content);
                    if (results.isEmpty()) {
                        endReached = true;
                    } else {
                        hits.onUpdateView(results, false);
                        lastDisplayedPage = lastRequestedPage;
                    }
                }
            }
        });
    }

    public List<Result> parseResults(final JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        List<Result> results = new ArrayList<>();
        JSONArray resultHits = jsonObject.optJSONArray("hits");
        if (resultHits == null) {
            return null;
        }

        for (int i = 0; i < resultHits.length(); ++i) {
            JSONObject hit = resultHits.optJSONObject(i);
            if (hit == null) {
                continue;
            }
            Result result = new Result();

            for (String attributeName : attributes.values()) {
                String attributeValue = hit.optString(attributeName);
                if (attributeValue == null) {
                    continue;
                }
                result.set(attributeName, attributeValue);

                final JSONObject highlightResult = hit.optJSONObject("_highlightResult");
                if (highlightResult != null) {
                    JSONObject highlightAttribute = highlightResult.optJSONObject(attributeName);
                    if (highlightAttribute != null) {
                        String highlightedValue = highlightAttribute.optString("value");
                        if (highlightedValue != null) {
                            result.set(attributeName, highlightedValue);
                        }
                    }
                }
            }
            results.add(result);
        }
        return results;
    }

    private void processActivity(final Activity activity) {
        View rootView = activity.getWindow().getDecorView().getRootView();
        searchBox = (SearchBox) rootView.findViewById(R.id.searchBox);
        if (searchBox == null) {
            throw new RuntimeException(Errors.LAYOUT_MISSING_SEARCHBOX);
        }

        hits = (Hits) rootView.findViewById(R.id.hits);
        if (hits == null) {
            throw new RuntimeException(Errors.LAYOUT_MISSING_HITS);
        }
        hits.setHelper(this);

        query.setHitsPerPage(hits.getHitsPerPage());

        // Link searchBox to the Activity's SearchableInfo
        SearchManager manager = (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);
        searchBox.setSearchableInfo(manager.getSearchableInfo(activity.getComponentName()));

        // Link hits to activity's empty view
        View emptyView = rootView.findViewById(R.id.empty);
        if (emptyView == null) {
            throw new RuntimeException(Errors.LAYOUT_MISSING_EMPTY);
        }
        hits.setEmptyView(emptyView);
        itemLayoutId = activity.getResources().getIdentifier(hits.getLayoutName(), "layout", activity.getPackageName());
    }

    /**
     * Tells if we should load more results when reaching the end of an {@link AlgoliaResultsView}
     *
     * @return true unless we reached the end of results or we already requested a new page
     */
    public boolean shouldLoadMore() {
        return !(endReached || lastRequestedPage > lastDisplayedPage);
    }
}
