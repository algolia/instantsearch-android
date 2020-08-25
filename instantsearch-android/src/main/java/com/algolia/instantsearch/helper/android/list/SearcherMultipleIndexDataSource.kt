package com.algolia.instantsearch.helper.android.list

import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.search.model.multipleindex.IndexQuery
import com.algolia.search.model.response.ResponseSearch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


public class SearcherMultipleIndexDataSource<T>(
    private val searcher: SearcherMultipleIndex,
    private val indexQuery: IndexQuery,
    private val transformer: (ResponseSearch.Hit) -> T
) : PageKeyedDataSource<Int, T>() {

    public class Factory<T>(
        private val searcher: SearcherMultipleIndex,
        private val indexQuery: IndexQuery,
        private val transformer: (ResponseSearch.Hit) -> T
    ) : DataSource.Factory<Int, T>() {

        override fun create(): DataSource<Int, T> {
            return SearcherMultipleIndexDataSource(searcher, indexQuery, transformer)
        }
    }

    private val index = searcher.queries.indexOf(indexQuery)
    private var initialLoadSize: Int = 30

    init {
        require(index != -1) { "The IndexQuery is not present in SearcherMultipleIndex" }
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, T>) {
        initialLoadSize = params.requestedLoadSize
        indexQuery.query.hitsPerPage = initialLoadSize
        indexQuery.query.page = 0
        searcher.isLoading.value = true
        runBlocking {
            try {
                val response = searcher.search()
                val result = response.results[index]
                val nextKey = if (result.nbHits > initialLoadSize) 1 else null

                withContext(searcher.coroutineScope.coroutineContext) {
                    searcher.response.value = response
                    searcher.isLoading.value = false
                }
                callback.onResult(result.hits.map(transformer), 0, result.nbHits, null, nextKey)
            } catch (throwable: Throwable) {
                resultError(throwable)
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        val initialOffset = (initialLoadSize / params.requestedLoadSize) - 1
        val page = params.key + initialOffset

        indexQuery.query.page = page
        indexQuery.query.hitsPerPage = params.requestedLoadSize
        searcher.isLoading.value = true
        runBlocking {
            try {
                val response = searcher.search()
                val result = response.results[index]
                val nextKey = if (page + 1 < result.nbPages) params.key + 1 else null

                withContext(searcher.coroutineScope.coroutineContext) {
                    searcher.response.value = response
                    searcher.isLoading.value = false
                }
                callback.onResult(result.hits.map(transformer), nextKey)
            } catch (throwable: Throwable) {
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
