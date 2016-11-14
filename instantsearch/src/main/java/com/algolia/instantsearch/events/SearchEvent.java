package com.algolia.instantsearch.events;

import com.algolia.search.saas.Query;

@SuppressWarnings("WeakerAccess")
public class SearchEvent {
    public final Query query;
    public final int requestSeqNumber;

    public SearchEvent(Query query, int requestSeqNumber) {
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
