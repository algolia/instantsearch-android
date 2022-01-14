package com.algolia.instantsearch.helper.android.list

import androidx.paging.DataSource
import com.algolia.instantsearch.helper.searcher.util.SearcherForHits
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.Query
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

/**
 * Data source for [SearcherForHits].
 */
public class HitsSearcherDataSource<T>(
    private val searcher: SearcherForHits<Query>,
    private val triggerSearchForQuery: ((Query) -> Boolean) = { true },
    retryDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val transformer: (ResponseSearch.Hit) -> T,
) : RetryablePageKeyedDataSource<Int, T>(retryDispatcher) {

    public class Factory<T>(
        private val searcher: SearcherForHits<Query>,
        private val triggerSearchForQuery: ((Query) -> Boolean) = { true },
        private val retryDispatcher: CoroutineDispatcher = Dispatchers.IO,
        private val transformer: (ResponseSearch.Hit) -> T,
    ) : DataSource.Factory<Int, T>() {

        override fun create(): DataSource<Int, T> {
            return HitsSearcherDataSource(
                searcher = searcher,
                triggerSearchForQuery = triggerSearchForQuery,
                retryDispatcher = retryDispatcher,
                transformer = transformer
            )
        }
    }

    private var initialLoadSize: Int = 30

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, T>) {
        val queryLoaded = searcher.query.query
        if (!triggerSearchForQuery(searcher.query)) return

        initialLoadSize = params.requestedLoadSize
        searcher.query.hitsPerPage = initialLoadSize
        searcher.query.page = 0
        searcher.isLoading.value = true
        runBlocking {
            try {
                val response = searcher.search()
                if (queryLoaded != searcher.query.query) {
                    invalidate()
                }
                val nextKey = if (response.hits.size >= initialLoadSize) 1 else null

                withContext(searcher.coroutineScope.coroutineContext) {
                    searcher.response.value = response
                    searcher.isLoading.value = false
                }
                retry = null
                callback.onResult(response.hits.map(transformer), 0, response.hits.size, null, nextKey)
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
                val response = searcher.search()
                val nextKey = if (page + 1 < response.nbPages) params.key + 1 else null

                withContext(searcher.coroutineScope.coroutineContext) {
                    searcher.response.value = response
                    searcher.isLoading.value = false
                }
                retry = null
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
