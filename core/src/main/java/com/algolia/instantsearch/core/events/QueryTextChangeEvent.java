package com.algolia.instantsearch.core.events;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * An event to let you react to changes in the search query.
 */
@SuppressWarnings("WeakerAccess")
public class QueryTextChangeEvent {
    /** The new query string. */
    @NonNull public final String query;
    /** The origin of the change. Can be a SearchView, an Intent, or a Searcher. */
    @Nullable public final Object origin;

    public QueryTextChangeEvent(final @NonNull String query, @Nullable Object origin) {
        this.query = query;
        this.origin = origin;
    }

    @Override public String toString() {
        return "QueryTextChangeEvent{" +
                "query='" + query + '\'' +
                ", origin=" + origin +
                '}';
    }
}
