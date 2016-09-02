package com.algolia.instantsearch.events;

import com.algolia.search.saas.Query;

public class SearchEvent {
    public final Query query;

    public SearchEvent(Query query) {
        this.query = query;
    }
}
