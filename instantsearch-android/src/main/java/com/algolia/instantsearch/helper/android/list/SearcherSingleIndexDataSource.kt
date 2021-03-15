package com.algolia.instantsearch.helper.android.list

import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.Query
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

public class SearcherSingleIndexDataSource<T>(
    private val searcher: SearcherSingleIndex,
    private val triggerSearchForQuery: ((Query) -> Boolean) = { true },
    private val transformer: (ResponseSearch.Hit) -> T,
) : PageKeyedDataSource<Int, T>() {

    public class Factory<T>(
        private val searcher: SearcherSingleIndex,
        private val triggerSearchForQuery: ((Query) -> Boolean) = { true },
        private val transformer: (ResponseSearch.Hit) -> T,
    ) : DataSource.Factory<Int, T>() {

        override fun create(): DataSource<Int, T> {
            return SearcherSingleIndexDataSource(searcher, triggerSearchForQuery, transformer)
        }
    }

    private var initialLoadSize: Int = 30
    private var retry: (() -> Any)? = null

    public fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.invoke()
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, T>) {
        val queryLoaded = searcher.query.query
        if (!triggerSearchForQuery(searcher.query)) return

        initialLoadSize = params.requestedLoadSize
        searcher.query.hitsPerPage = initialLoadSize
        searcher.query.page = 0
        searcher.isLoading.value = true
        runBlocking {
            try {
                retry = null
                val response = searcher.search()
                if (queryLoaded != searcher.query.query) {
                    invalidate()
                }
                val nextKey = if (response.nbHits > initialLoadSize) 1 else null

                withContext(searcher.coroutineScope.coroutineContext) {
                    searcher.response.value = response
                    searcher.isLoading.value = false
                }
                callback.onResult(response.hits.map(transformer), 0, response.nbHits, null, nextKey)
            } catch (throwable: Throwable) {
                retry = { loadInitial(params, callback) }
                resultError(throwable)
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        val initialOffset = (initialLoadSize / params.requestedLoadSize) - 1
        val page = params.key + initialOffset

        searcher.query.page = page
        searcher.query.hitsPerPage = params.requestedLoadSize
        searcher.isLoading.value = true
        runBlocking {
            try {
                retry = null
                val response = searcher.search()
                val nextKey = if (page + 1 < response.nbPages) params.key + 1 else null

                withContext(searcher.coroutineScope.coroutineContext) {
                    searcher.response.value = response
                    searcher.isLoading.value = false
                }
                callback.onResult(response.hits.map(transformer), nextKey)
            } catch (throwable: Throwable) {
                retry = { loadAfter(params, callback) }
                resultError(throwable)
            }
        }
    }

    private suspend fun resultError(throwable: Throwable) {
        withContext(searcher.coroutineScope.coroutineContext) {
            searcher.error.value = throwable
            searcher.isLoading.value = false
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, T>): Unit = Unit
}
