package com.algolia.instantsearch.strategies;

import com.algolia.instantsearch.Searcher;

/**
 * This {@link SearchStrategy} only fires requests on demand.
 */
public class OnDemandStrategy implements SearchStrategy {
    private final Searcher searcher;

    public OnDemandStrategy(Searcher searcher) {
        this.searcher = searcher;
    }

    @Override
    public boolean beforeSearch(final Searcher searcher, final String queryString) {
        searcher.postErrorEvent("OnDemandStrategy: Search currently disabled.");
        return false;
    }

    /**
     * Triggers a search request with the current searcher's state.
     */
    public void search() {
        searcher.search();
    }
}