package com.algolia.instantsearch.core.events;

import android.support.annotation.NonNull;

import com.algolia.instantsearch.core.helpers.Searcher;

/** An event that is triggered by a Searcher instance. */
abstract class SearcherEvent {
    /** The Searcher that triggered this event. */
    @NonNull public final Searcher searcher;

    SearcherEvent(@NonNull Searcher searcher) {
        this.searcher = searcher;
    }
}
