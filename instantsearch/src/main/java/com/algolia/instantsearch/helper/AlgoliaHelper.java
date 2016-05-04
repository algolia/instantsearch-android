package com.algolia.instantsearch.helper;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.algolia.instantsearch.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AlgoliaHelper {
    static final int DEFAULT_HITS_PER_PAGE = 20;
    public static final String DEFAULT_ATTRIBUTES = "objectID";

    private final Index index;
    private final Client client;
    private final Query query;
    private SearchBox searchBox;
    private Hits hits;

    public AlgoliaHelper(final String applicationId, final String apiKey, final String indexName) {
        client = new Client(applicationId, apiKey);
        index = client.initIndex(indexName);
        query = new Query();
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

    public List<String> parseResults(final JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        List<String> results = new ArrayList<>();
        JSONArray resultHits = jsonObject.optJSONArray("hits");
        if (resultHits == null) {
            return null;
        }

        for (int i = 0; i < resultHits.length(); ++i) {
            JSONObject hit = resultHits.optJSONObject(i);
            if (hit == null) {
                continue;
            }

            JSONObject highlightResult = hit.optJSONObject("_highlightResult");
            if (highlightResult == null) {
                continue;
            }
            String attribute = hit.optString(hits.getAttributeToDisplay());
            if (attribute == null) {
                continue;
            }

            results.add(attribute);
        }
        return results;
    }

    public void setActivity(Activity activity) {
        View rootView = activity.getWindow().getDecorView().getRootView();
        searchBox = (SearchBox) rootView.findViewById(R.id.searchBox);
        hits = (Hits) rootView.findViewById(R.id.hits);
        query.setHitsPerPage(hits.getHitsPerPage());
        query.setAttributesToRetrieve(hits.getAttributesToRetrieve());
        query.setAttributesToHighlight(hits.getAttributesToHighlight());

        SearchManager manager = (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);
        searchBox.setSearchableInfo(manager.getSearchableInfo(activity.getComponentName()));
    }
}
