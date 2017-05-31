package com.algolia.instantsearch.events;

import com.algolia.instantsearch.model.SearchResults;
import com.algolia.search.saas.Query;

import org.json.JSONObject;

/**
 * An event to let you react to new search results.
 */
@SuppressWarnings("WeakerAccess")
public class ResultEvent {
    /** the search results. */
    public final SearchResults results;
    /** the Query that was sent with the search request. */
    public final Query query;
    /** the search request's identifier. */
    public final int requestSeqNumber;

    public ResultEvent(JSONObject json, Query query, int requestSeqNumber) {
        this.results = new SearchResults(json);
        this.query = query;
        this.requestSeqNumber = requestSeqNumber;
    }

    @Override
    public String toString() {
        return "ResultEvent{" +
                "requestSeqNumber=" + requestSeqNumber +
                ", content=" + results +
                ", query=" + query +
                '}';
    }
}
