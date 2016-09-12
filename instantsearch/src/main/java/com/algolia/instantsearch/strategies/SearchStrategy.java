package com.algolia.instantsearch.strategies;

public interface SearchStrategy {
    /**
     * This method will be called to determine if a search should be performed.
     *
     * @param searcher    a reference to the searcher that would perform the search.
     * @param queryString the current query text for this request.
     * @return {@code true} if the search request should be sent.
     */
    boolean search(com.algolia.instantsearch.Searcher searcher, String queryString);
}
