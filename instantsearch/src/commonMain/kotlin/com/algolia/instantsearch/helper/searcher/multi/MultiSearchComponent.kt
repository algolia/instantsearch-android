package com.algolia.instantsearch.helper.searcher.multi

import com.algolia.instantsearch.core.searcher.Searcher

/**
 * Multi Searcher Component providing a list of requests to perform, and get a list of results as a response.
 */
public interface MultiSearchComponent<Request, Response> : Searcher<Response> {

    /**
     * Returns the list of queries and the completion that might be called with for the result of these queries.
     */
    public fun collect(): Pair<List<Request>, (Result<List<Response>>) -> Unit>
}
