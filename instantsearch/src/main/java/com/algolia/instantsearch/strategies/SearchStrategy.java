package com.algolia.instantsearch.strategies;

import com.algolia.instantsearch.Searcher;

public interface SearchStrategy {
    /**
     * This method will be called to determine if a search should be performed.
     * If not, use {@link Searcher#postErrorEvent(String)} to describe why the request is denied.
     *
     * @param searcher    a reference to the searcher that would perform the search.
     * @param queryString the current query text for this request.
     * @return {@code true} if the search request should be sent.
     */
    boolean beforeSearch(Searcher searcher, String queryString);
}
