package com.algolia.instantsearch.compose.paging.internal

import com.algolia.instantsearch.compose.paging.Paginator

/**
 * [Paginator] for searchers.
 */
internal interface SearcherPaginator<T : Any> : Paginator<T> {

    /**
     * Callback when the searcher got changes.
     */
    fun onSearcherChange(callback: () -> Unit)
}
