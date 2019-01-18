package com.algolia.instantsearch.core.events;

import com.algolia.instantsearch.core.helpers.Searcher;
import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Query;

import androidx.annotation.NonNull;

/**
 * An event to let you react to search errors.
 */
@SuppressWarnings("WeakerAccess")
public class ErrorEvent extends SearcherEvent {
    /** The error that was received. */
    @NonNull public final AlgoliaException error;
    /** the Query that was sent with the search request. */
    @NonNull public final Query query;
    /** the search request's identifier. */
    public final int requestSeqNumber;

    public ErrorEvent(@NonNull final Searcher searcher,
                      @NonNull final AlgoliaException error,
                      @NonNull final Query query,
                      int requestSeqNumber) {
        super(searcher);
        this.error = error;
        this.query = query;
        this.requestSeqNumber = requestSeqNumber;
    }

    @Override public String toString() {
        return "ErrorEvent{" +
                "searcher=" + searcher +
                ", error=" + error +
                ", query=" + query +
                ", requestSeqNumber=" + requestSeqNumber +
                '}';
    }
}
