package com.algolia.instantsearch.helper.searcher.multi

import com.algolia.instantsearch.helper.searcher.multi.internal.MultiSearchComponent
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.multipleindex.IndexedQuery
import com.algolia.search.model.response.ResultSearch

/**
 * Abstract implementation of [MultiSearcher].
 */
public abstract class AbstractMultiSearcher : MultiSearcher {

    /**
     * Client to perform operations on indices.
     */
    internal abstract val client: ClientSearch

    /**
     * Adds a search component to the multi-searcher.
     */
    internal abstract fun addSearcher(component: MultiSearchComponent<IndexedQuery, ResultSearch>): MultiSearchComponent<IndexedQuery, ResultSearch>
}
