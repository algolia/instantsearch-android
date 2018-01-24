package com.algolia.instantsearch.events;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * An event to let you react to changes in the search query.
 */
@SuppressWarnings("WeakerAccess")
public class QueryTextChangeEvent {
    /** The new query string. */
    public final @NonNull String query;
    /** The origin of the change. Can be a SearchView, an Intent, or a Searcher. */
    public @Nullable final Object origin;

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
