package com.algolia.instantsearch.helper.searcher.multi.internal

import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.search.model.multipleindex.IndexedQuery
import com.algolia.search.model.response.ResultSearch

/**
 * Multi Searcher Component providing a list of requests to perform, and get a list of results as a response.
 */
internal interface MultiSearchComponent<out Request : IndexedQuery, Response : ResultSearch> : Searcher<Response> {

    /**
     * The component query as [IndexedQuery]
     */
    public val indexedQuery: Request

    /**
     * Collects requests list and a callback for responses list.
     */
    public fun collect(): Pair<List<Request>, (List<Response>) -> Unit>
}

/**
 * [MultiSearchComponent] generics cast.
 */
@Suppress("UNCHECKED_CAST")
internal fun <T : IndexedQuery, K : ResultSearch> MultiSearchComponent<T, K>.asMultiSearchComponent() =
    this as MultiSearchComponent<IndexedQuery, ResultSearch>
