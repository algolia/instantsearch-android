package com.algolia.instantsearch.core.searchclient;

import com.algolia.search.saas.Query;

import org.json.JSONObject;

import androidx.annotation.NonNull;

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
