package com.algolia.instantsearch.core.events;

import com.algolia.instantsearch.core.helpers.Searcher;

import androidx.annotation.NonNull;

/**
 * An event to let you react to resetting of the search interface.
 */
public class ResetEvent extends SearcherEvent {
    public ResetEvent(@NonNull Searcher searcher) {
        super(searcher);
    }

    @Override public String toString() {
        return "ResetEvent{" +
                "searcher=" + searcher +
                '}';
    }
}
