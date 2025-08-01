package com.algolia.instantsearch.android.paging3.internal

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.algolia.instantsearch.extension.Console
import com.algolia.instantsearch.searcher.SearcherForHits
import com.algolia.search.model.params.SearchParameters
import com.algolia.search.model.response.ResponseSearch

/**
 * Implementation of [PagingSource] with [SearcherForHits].
 *
 * @param searcher single index searcher
 * @param transformer mapping applied to search responses
 */
internal class SearcherPagingSource<T : Any>(
    private val searcher: SearcherForHits<out SearchParameters>,
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

            val response = search() ?: return emptyPage()
            val data = response.hits.map(transformer)
            val nextKey = if (pageNumber < response.nbPages - 1) pageNumber + 1 else null
            LoadResult.Page(
                data = data,
                prevKey = null, // no paging backward
                nextKey = nextKey
            )
        } catch (exception: Exception) {
            Console.warn("Paging search operation failed", exception)
            LoadResult.Error(exception)
        }
    }

    private suspend fun search(): ResponseSearch? = with(searcher) {
        try {
            isLoading.value = true
            val searchResponse = search()
            response.value = searchResponse
            return searchResponse
        } catch (exception: Exception) {
            error.value = exception
            throw exception
        } finally {
            isLoading.value = false
        }
    }

    private fun <T : Any> emptyPage() = LoadResult.Page<Int, T>(emptyList(), null, null)
}
