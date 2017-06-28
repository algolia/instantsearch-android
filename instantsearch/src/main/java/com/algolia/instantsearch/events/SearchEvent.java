package com.algolia.instantsearch.events;

import com.algolia.search.saas.Query;

/**
 * An event to let you react to new search requests.
 */
@SuppressWarnings("WeakerAccess")
public class SearchEvent {
    /** the Query that was sent with the search request. */
    public final Query query;
    /** the search request's identifier. */
    public final int requestSeqNumber;

    public SearchEvent(final Query query, final int requestSeqNumber) {
        this.query = query;
        this.requestSeqNumber = requestSeqNumber;
    }

    @Override
    public String toString() {
        return "SearchEvent{" +
                "requestSeqNumber=" + requestSeqNumber +
                ", query=" + query +
                '}';
    }
}
