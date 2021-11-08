package com.algolia.instantsearch.compose.list.internal

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.algolia.instantsearch.ExperimentalInstantSearch
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.response.ResponseSearch
import kotlinx.coroutines.withContext

/**
 * Implementation of [PagingSource] with [SearcherSingleIndex].
 *
 * @param searcher single index searcher
 * @param transformer mapping applied to search responses
 */
@ExperimentalInstantSearch
internal class SearcherSingleIndexPagingSource<T : Any>(
    private val searcher: SearcherSingleIndex,
    private val transformer: (ResponseSearch.Hit) -> T
) : PagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>): Int {
        return 0 // on refresh (for new query), start from the first page (number zero)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return try {
            val pageNumber = params.key ?: 0
            searcher.query.page = pageNumber
            searcher.query.hitsPerPage = params.loadSize

            val response = search()
            val data = response.hits.map(transformer)
            val nextKey = if (pageNumber < response.nbPages) pageNumber + 1 else null
            LoadResult.Page(
                data = data,
                prevKey = null, // no paging backward
                nextKey = nextKey
            )
        } catch (throwable: Throwable) {
            LoadResult.Error(throwable)
        }
    }

    private suspend fun search(): ResponseSearch {
        try {
            searcher.isLoading.value = true
            val response = searcher.search()
            withContext(searcher.coroutineScope.coroutineContext) {
                searcher.response.value = response
                searcher.isLoading.value = false
            }
            return response
        } catch (throwable: Throwable) {
            withContext(searcher.coroutineScope.coroutineContext) {
                searcher.error.value = throwable
                searcher.isLoading.value = false
            }
            throw throwable
        }
    }
}
