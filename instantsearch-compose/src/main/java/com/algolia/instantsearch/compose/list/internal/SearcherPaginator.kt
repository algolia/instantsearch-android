package com.algolia.instantsearch.compose.list.internal

import androidx.paging.InvalidatingPagingSourceFactory
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import com.algolia.instantsearch.compose.list.Paginator
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.multipleindex.IndexQuery
import com.algolia.search.model.response.ResponseSearch

/**
 * [Paginator] implementation for Searcher.
 *
 * @param pagingConfig configure loading behavior within a Pager
 * @param pagingSourceFactory searcher paging source factory
 */
internal abstract class SearcherPaginator<T : Any>(
    pagingConfig: PagingConfig = PagingConfig(pageSize = 10),
    pagingSourceFactory: () -> PagingSource<Int, T>
) : Paginator<T> {

    private val pagingSourceFactory = InvalidatingPagingSourceFactory(pagingSourceFactory)

    override val flow = Pager(config = pagingConfig, pagingSourceFactory = pagingSourceFactory).flow

    override fun invalidate() {
        pagingSourceFactory.invalidate()
    }
}

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
    transformer: (ResponseSearch.Hit) -> T
) : SearcherPaginator<T>(
    pagingConfig = pagingConfig,
    pagingSourceFactory = { SearcherSingleIndexPagingSource(searcher, transformer) }
)


/**
 * [Paginator] implementation for [SearcherMultipleIndex].
 *
 * @param searcher multiple index searcher
 * @param indexQuery query associated to a specific index
 * @param pagingConfig configure loading behavior within a Pager
 * @param transformer mapping applied to search responses
 */
internal class SearcherMultipleIndexPaginator<T : Any>(
    searcher: SearcherMultipleIndex,
    indexQuery: IndexQuery,
    pagingConfig: PagingConfig = PagingConfig(pageSize = 10),
    transformer: (ResponseSearch.Hit) -> T
) : SearcherPaginator<T>(
    pagingConfig = pagingConfig,
    pagingSourceFactory = { SearcherMultipleIndexPagingSource(searcher, indexQuery, transformer) }
)
