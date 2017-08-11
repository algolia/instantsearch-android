package com.algolia.instantsearch.events;


/**
 * An event to let you react to changes in the SearchView's query.
 */
@SuppressWarnings("WeakerAccess")
public class QueryTextChangeEvent {
    public final String newText;

    public QueryTextChangeEvent(final String newText) {
        this.newText = newText;
    }
}
