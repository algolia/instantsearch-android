package com.algolia.instantsearch.events;

import com.algolia.search.saas.Query;

import org.json.JSONObject;

/**
 * An event to let you react to new search results.
 */
public class ResultEvent {
    /** the JSON content containing these results. */
    public final JSONObject content;
    /** the Query that was sent with the search request. */
    public final Query query;
    /** the search request's identifier. */
    public final int requestSeqNumber;

    public ResultEvent(JSONObject content, Query query, int requestSeqNumber) {
        this.content = content;
        this.query = query;
        this.requestSeqNumber = requestSeqNumber;
    }

    @Override
    public String toString() {
        return "ResultEvent{" +
                "requestSeqNumber=" + requestSeqNumber +
                ", content=" + content +
                ", query=" + query +
                '}';
    }
}
