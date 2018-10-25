package com.algolia.instantsearch.core.searchclient;

import android.support.annotation.NonNull;

import com.algolia.search.saas.Query;

import org.json.JSONObject;

public class DefaultSearchClient extends SearchClient<Query, JSONObject> {

    @Override
    public Query map(@NonNull Query query) {
        return query;
    }

    @Override
    public JSONObject map(@NonNull JSONObject customSearchResults) {
        return customSearchResults;
    }
}
