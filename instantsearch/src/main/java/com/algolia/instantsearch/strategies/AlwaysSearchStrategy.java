package com.algolia.instantsearch.strategies;

import com.algolia.instantsearch.Searcher;

/**
 * This {@link SearchStrategy} always fires requests.
 */
public class AlwaysSearchStrategy implements SearchStrategy {
    @Override
    public boolean beforeSearch(Searcher searcher, String queryString) {
        return true;
    }
}
