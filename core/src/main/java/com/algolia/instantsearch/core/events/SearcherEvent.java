package com.algolia.instantsearch.core.events;

import com.algolia.instantsearch.core.helpers.Searcher;

import androidx.annotation.NonNull;

/**
 * An event that is triggered by a Searcher instance.
 */
abstract class SearcherEvent {
    /**
     * The Searcher that triggered this event.
     */
    @NonNull
    public final Searcher searcher;

    SearcherEvent(@NonNull Searcher searcher) {
        this.searcher = searcher;
    }
}
