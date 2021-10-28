package com.algolia.instantsearch.helper.searcher.multi.internal

import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.search.model.multipleindex.IndexedQuery
import com.algolia.search.model.response.ResultSearch

/**
 * Multi Searcher Component providing a list of requests to perform, and get a list of results as a response.
 */
internal interface MultiSearchComponent<out Request : IndexedQuery, Response : ResultSearch> : Searcher<Response> {


    /**
     * Collects requests list and a callback for responses list.
     */
    fun collect(): Pair<List<Request>, (List<Response>) -> Unit>
}

/**
 * [MultiSearchComponent] generics cast.
 */
@Suppress("UNCHECKED_CAST")
internal fun <T : IndexedQuery, K : ResultSearch> MultiSearchComponent<T, K>.asMultiSearchComponent() =
    this as MultiSearchComponent<IndexedQuery, ResultSearch>
