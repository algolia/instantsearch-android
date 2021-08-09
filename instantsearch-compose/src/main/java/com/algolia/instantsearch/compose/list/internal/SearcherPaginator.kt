package com.algolia.instantsearch.compose.list.internal

import androidx.paging.InvalidatingPagingSourceFactory
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import com.algolia.instantsearch.compose.list.Paginator

/**
 * [Paginator] implementation for Searcher.
 *
 * @param pagingConfig configure loading behavior within a Pager
 * @param pagingSourceFactory searcher paging source factory
 */
internal class SearcherPaginator<T : Any>(
    pagingConfig: PagingConfig = PagingConfig(pageSize = 10),
    pagingSourceFactory: () -> PagingSource<Int, T>
) : Paginator<T> {

    private val invalidatingPagingSourceFactory = InvalidatingPagingSourceFactory(pagingSourceFactory)

    override val flow = Pager(config = pagingConfig, pagingSourceFactory = invalidatingPagingSourceFactory).flow

    override fun invalidate() {
        invalidatingPagingSourceFactory.invalidate()
    }
}
