package com.algolia.instantsearch.events;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Query;

/**
 * An event to let you react to search errors.
 */
@SuppressWarnings("WeakerAccess")
public class ErrorEvent {
    /** The error that was received. */
    public final AlgoliaException error;
    /** the Query that was sent with the search request. */
    public final Query query;
    /** the search request's identifier. */
    public final int requestSeqNumber;

    public ErrorEvent(final AlgoliaException error, final Query query, int requestSeqNumber) {
        this.error = error;
        this.query = query;
        this.requestSeqNumber = requestSeqNumber;
    }

    @Override
    public String toString() {
        return "ErrorEvent{" +
                "requestSeqNumber=" + requestSeqNumber +
                ", query=" + query +
                ", error=" + error +
                '}';
    }
}
