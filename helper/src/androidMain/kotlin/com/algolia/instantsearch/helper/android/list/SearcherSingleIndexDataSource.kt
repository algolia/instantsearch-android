package com.algolia.instantsearch.helper.android.list

import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.response.ResponseSearch
import kotlinx.coroutines.runBlocking


public class SearcherSingleIndexDataSource<T>(
    private val searcher: SearcherSingleIndex,
    private val transformer: (ResponseSearch.Hit) -> T
) : PageKeyedDataSource<Int, T>() {

    public class Factory<T>(
        private val searcher: SearcherSingleIndex,
        private val transformer: (ResponseSearch.Hit) -> T
    ) : DataSource.Factory<Int, T>() {

        override fun create(): DataSource<Int, T> {
            return SearcherSingleIndexDataSource(searcher, transformer)
        }
    }

    public val error = SubscriptionValue<Throwable?>(null)

    private var initialLoadSize: Int = 30

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, T>) {
        initialLoadSize = params.requestedLoadSize
        searcher.query.hitsPerPage = initialLoadSize
        searcher.query.page = 0
        runBlocking {
            try {
                val response = searcher.search()
                val nextKey = if (response.nbHits > initialLoadSize) 1 else null

                callback.onResult(response.hits.map(transformer), 0, response.nbHits, null, nextKey)
            } catch (throwable: Throwable) {
                error.value = throwable
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        val initialOffset = (initialLoadSize / params.requestedLoadSize) - 1
        val page = params.key + initialOffset

        searcher.query.page = page
        searcher.query.hitsPerPage = params.requestedLoadSize
        runBlocking {
            try {
                val response = searcher.search()
                val nextKey = if (page + 1 < response.nbPages) params.key + 1 else null

                callback.onResult(response.hits.map(transformer), nextKey)
            } catch (throwable: Exception) {
                error.value = throwable
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, T>) = Unit
}