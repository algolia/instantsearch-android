package com.algolia.instantsearch;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.util.Log;
import android.view.View;

import com.algolia.instantsearch.model.Highlight;
import com.algolia.instantsearch.model.Result;
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
    public static final int DEFAULT_HITS_PER_PAGE = 20;
    public static final String DEFAULT_ATTRIBUTES = "objectID";

    private static final Map<Integer, String> attributes = new HashMap<>();
    private static int itemLayoutId;

    private final Index index;
    private final Client client;
    private final Query query;
    private SearchBox searchBox;
    private Hits hits;

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
        String existingAttribute = attributes.get(id);
        if (existingAttribute == null) {
            attributes.put(id, attributeName);
        }
    }

    public static int getItemLayoutId() {
        return itemLayoutId;
    }

    public void search(final String queryString, final CompletionHandler listener) {
        query.setQuery(queryString);
        index.searchAsync(query, new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject content, AlgoliaException error) {
                if (error == null) {
                    Log.d("PLN|search.searchResult", String.format("Index %s with query %s succeeded: %s.", index.getIndexName(), queryString, content));
                } else {
                    Log.e("PLN|search.searchError", String.format("Index %s with query %s failed: %s(%s).", index.getIndexName(), queryString, error.getCause(), error.getMessage()));

                }
                listener.requestCompleted(content, error);
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
                        String value = highlightAttribute.optString("value");
                        if (value != null) {
                            result.addHighlight(new Highlight(attributeName, value));
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
            throw new RuntimeException(activity.getString(R.string.error_missing_searchbox));
        }

        hits = (Hits) rootView.findViewById(R.id.hits);
        if (hits == null) {
            throw new RuntimeException(activity.getString(R.string.error_missing_hits));
        }

        query.setHitsPerPage(hits.getHitsPerPage());
        query.setAttributesToRetrieve(hits.getAttributesToRetrieve());
        query.setAttributesToHighlight(hits.getAttributesToHighlight());

        // Link searchBox to the Activity's SearchableInfo
        SearchManager manager = (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);
        searchBox.setSearchableInfo(manager.getSearchableInfo(activity.getComponentName()));

        // Link hits to activity's empty view
        View emptyView = rootView.findViewById(R.id.empty);
        if (emptyView == null) {
            throw new RuntimeException(activity.getString(R.string.error_missing_empty));
        }
        hits.setEmptyView(emptyView);
        itemLayoutId = activity.getResources().getIdentifier(hits.getLayoutName(), "layout", activity.getPackageName());
    }
}
