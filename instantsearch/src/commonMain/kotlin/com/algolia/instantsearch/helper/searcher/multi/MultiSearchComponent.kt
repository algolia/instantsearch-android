package com.algolia.instantsearch.helper.searcher.multi

import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.search.model.multipleindex.IndexedQuery
import com.algolia.search.model.response.ResultSearch

/**
 * Multi Searcher Component providing a list of requests to perform, and get a list of results as a response.
 */
public interface MultiSearchComponent<out Request : IndexedQuery, Response : ResultSearch> : Searcher<Response> {

    /**
     * The component query as [IndexedQuery]
     */
    public val indexedQuery: Request

    /**
     * Collects requests list and a callback for responses list.
     */
    public fun collect(): Pair<List<Request>, (List<Response>) -> Unit>
}
