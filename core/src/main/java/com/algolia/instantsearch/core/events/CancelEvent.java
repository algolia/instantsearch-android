package com.algolia.instantsearch.core.events;


import com.algolia.instantsearch.core.helpers.Searcher;
import com.algolia.search.saas.Request;

import androidx.annotation.NonNull;

/**
 * An event to let you react to cancellation of search requests.
 */
@SuppressWarnings("WeakerAccess")
public class CancelEvent extends SearcherEvent {
    /** The request that has been cancelled.*/
    @NonNull public final Request request;
    /** the search request's identifier. */
    public final int requestSeqNumber;

    public CancelEvent(@NonNull final Searcher searcher,
                       @NonNull final Request request,
                       int requestSeqNumber) {
        super(searcher);
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
