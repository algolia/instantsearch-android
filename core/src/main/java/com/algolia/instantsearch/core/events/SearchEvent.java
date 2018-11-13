package com.algolia.instantsearch.core.events;

import com.algolia.instantsearch.core.helpers.Searcher;
import com.algolia.search.saas.Query;

import androidx.annotation.NonNull;

/**
 * An event to let you react to new search requests.
 */
@SuppressWarnings("WeakerAccess")
public class SearchEvent extends SearcherEvent {
    /** the Query that was sent with the search request. */
    @NonNull public final Query query;
    /** the search request's identifier. */
    public final int requestSeqNumber;

    public SearchEvent(@NonNull Searcher searcher,
                       @NonNull final Query query,
                       final int requestSeqNumber) {
        super(searcher);
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
