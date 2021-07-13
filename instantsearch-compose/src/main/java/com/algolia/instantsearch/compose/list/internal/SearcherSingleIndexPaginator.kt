package com.algolia.instantsearch.compose.list.internal

import androidx.paging.InvalidatingPagingSourceFactory
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.algolia.instantsearch.compose.list.Paginator
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.response.ResponseSearch

/**
 * [Paginator] implementation for [SearcherSingleIndex].
 *
 * @param searcher single index searcher
 * @param pagingConfig configure loading behavior within a Pager
 * @param transformer mapping applied to search responses
 */
internal class SearcherSingleIndexPaginator<T : Any>(
    searcher: SearcherSingleIndex,
    pagingConfig: PagingConfig = PagingConfig(pageSize = 10),
    transformer: (ResponseSearch) -> List<T>
) : Paginator<T> {

    private val pagingSourceFactory = InvalidatingPagingSourceFactory { SearcherSingleIndexPagingSource(searcher, transformer) }
    override val flow = Pager(config = pagingConfig, pagingSourceFactory = pagingSourceFactory).flow

    override fun invalidate() {
        pagingSourceFactory.invalidate()
    }
}
