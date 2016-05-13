package com.algolia.instantsearch.helper;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.util.Log;
import android.view.View;

import com.algolia.instantsearch.R;
import com.algolia.instantsearch.helper.databinding.Result;
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
import java.util.Set;

public class AlgoliaHelper {
    public static final int DEFAULT_HITS_PER_PAGE = 20;
    public static final String DEFAULT_ATTRIBUTES = "objectID";

    private static HashMap<Integer, String> attributesMap = new HashMap<>();
    private static Context context;
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

    public static Collection<String> getAttributes() {
        return attributesMap.values();
    }

    public static Set<Map.Entry<Integer, String>> getEntrySet() {
        return attributesMap.entrySet();
    }

    @SuppressWarnings("unused") // called via Data Binding
    @BindingAdapter({"attribute"})
    public static void bindAttribute(View view, String attributeName) {
        final int id = view.getId();
        final String entryName = context.getResources().getResourceEntryName(id);
        String existingAttribute = attributesMap.get(id);
        if (existingAttribute == null) {
            attributesMap.put(id, attributeName);
        }
    }

    public void search(final String queryString, final CompletionHandler listener) {
        query.setQuery(queryString);
        index.searchAsync(query, new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject content, AlgoliaException error) {
                if (error == null) {
                    Log.d("PLN|search.searchResult", String.format("Index %s with query %s succeeded: %s.", index.getIndexName(), queryString, content));
                } else {
                    Log.d("PLN|search.searchError", String.format("Index %s with query %s failed: %s(%s).", index.getIndexName(), queryString, error.getCause(), error.getMessage()));

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

            JSONObject highlightResult = hit.optJSONObject("_highlightResult");
            if (highlightResult == null) {
                continue;
            }
            for (String boundAttribute : AlgoliaHelper.getAttributes()) {
                String resultAttribute = hit.optString(boundAttribute);
                if (resultAttribute == null) {
                    continue;
                }
                result.set(boundAttribute, resultAttribute);
            }
            results.add(result);
        }
        return results;
    }

    private void processActivity(final Activity activity) {
        context = activity;
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
        View emptyView = rootView.findViewById(android.R.id.empty);
        if (emptyView == null) {
            throw new RuntimeException(activity.getString(R.string.error_missing_empty));
        }
        hits.setEmptyView(emptyView);
    }
}
