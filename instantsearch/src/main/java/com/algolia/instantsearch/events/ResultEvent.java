package com.algolia.instantsearch.events;

import com.algolia.instantsearch.model.SearchResults;
import com.algolia.search.saas.Query;

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

    public static final int REQUEST_UNKNOWN = -1;

    public ResultEvent(final SearchResults results, final Query query, final int requestSeqNumber) {
        this.results = results;
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
