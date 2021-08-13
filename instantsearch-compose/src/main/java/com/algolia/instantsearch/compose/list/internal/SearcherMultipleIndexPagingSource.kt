package com.algolia.instantsearch.compose.list.internal

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.search.model.multipleindex.IndexQuery
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.response.ResponseSearches
import kotlinx.coroutines.withContext

/**
 * Implementation of [PagingSource] with [SearcherMultipleIndex].
 *
 * @param searcher multiple index searcher
 * @param indexQuery query associated to a specific index
 * @param transformer mapping applied to search responses
 */
internal class SearcherMultipleIndexPagingSource<T : Any>(
    private val searcher: SearcherMultipleIndex,
    private val indexQuery: IndexQuery,
    private val transformer: (ResponseSearch.Hit) -> T
) : PagingSource<Int, T>() {

    private val index = searcher.queries.indexOf(indexQuery)

    init {
        require(index != -1) { "The IndexQuery is not present in SearcherMultipleIndex" }
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int {
        return 0 // on refresh (for new query), start from the first page (number zero)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return try {
            val pageNumber = params.key ?: 0
            indexQuery.query.page = pageNumber
            indexQuery.query.hitsPerPage = params.loadSize

            val response = search()
            val result = response.results[index]
            val data = result.hits.map(transformer)
            val nextKey = if (pageNumber < result.nbPages) pageNumber + 1 else null
            LoadResult.Page(
                data = data,
                prevKey = null, // no paging backward
                nextKey = nextKey
            )
        } catch (throwable: Throwable) {
            LoadResult.Error(throwable)
        }
    }

    private suspend fun search(): ResponseSearches {
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
