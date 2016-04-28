package algolia.com.searchdemo.helper;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.algolia.search.saas.APIClient;
import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.algolia.search.saas.listeners.SearchListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import algolia.com.searchdemo.R;

public class AlgoliaHelper {
    private static final int HITS_PER_PAGE = 20;

    private final Index index;
    private final APIClient client;
    private final Query query;
    private Activity activity;
    private View rootView;
    private SearchBox searchBox;

    public AlgoliaHelper(final String applicationId, final String apiKey, final String indexName) {
        client = new APIClient(applicationId, apiKey);
        index = client.initIndex(indexName);
        query = new Query();
        query.setAttributesToRetrieve("name", "company", "country", "followers");
        query.setAttributesToHighlight("name", "company");
        query.setHitsPerPage(HITS_PER_PAGE);
    }


    public void search(final String queryString, final SearchListener listener) {
        SearchListener logListener = new SearchListener() {
            @Override
            public void searchResult(Index index, Query query, JSONObject results) {
                Log.d("PLN|search.searchResult", String.format("Index %s with query %s succeeded: %s.", index.getIndexName(), queryString, results));
                listener.searchResult(index, query, results);
            }

            @Override
            public void searchError(Index index, Query query, AlgoliaException e) {
                Log.d("PLN|search.searchError", String.format("Index %s with query %s failed: %s(%s).", index.getIndexName(), queryString, e.getCause(), e.getMessage()));
                listener.searchError(index, query, e);
            }
        };
        query.setQueryString(queryString);
        index.searchASync(query, logListener);
    }

    public List<String> parseResults(final JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        List<String> results = new ArrayList<>();
        JSONArray hits = jsonObject.optJSONArray("hits");
        if (hits == null) {
            return null;
        }

        for (int i = 0; i < hits.length(); ++i) {
            JSONObject hit = hits.optJSONObject(i);
            if (hit == null) {
                continue;
            }

            JSONObject highlightResult = hit.optJSONObject("_highlightResult");
            if (highlightResult == null) {
                continue;
            }
            String name;
            try {
                name = highlightResult.getString("name");
            } catch (JSONException e) {
                continue;
            }
            if (name == null) {
                continue;
            }

            results.add(name);
        }
        return results;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
        rootView = activity.getWindow().getDecorView().getRootView();
        searchBox = (SearchBox) rootView.findViewById(R.id.searchBox);

        SearchManager manager = (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);
        searchBox.setSearchableInfo(manager.getSearchableInfo(activity.getComponentName()));
    }
}
