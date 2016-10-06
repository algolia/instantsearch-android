package com.algolia.instantsearch.events;

import com.algolia.search.saas.Request;

/**
 * An event to let you react to cancellation of search requests.
 */
public class CancelEvent {
    /** The request that has been cancelled.*/
    public final Request request;
    /** the search request's identifier. */
    public final Integer requestSeqNumber;

    public CancelEvent(Request request, Integer requestSeqNumber) {
        this.request = request;
        this.requestSeqNumber = requestSeqNumber;
    }

    @Override
    public String toString() {
        return "CancelEvent{" +
                "requestSeqNumber=" + requestSeqNumber +
                ", request=" + request +
                '}';
    }
}
