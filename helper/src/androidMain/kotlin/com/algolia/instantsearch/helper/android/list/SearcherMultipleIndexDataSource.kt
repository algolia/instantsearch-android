package com.algolia.instantsearch.helper.android.list

import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.search.model.multipleindex.IndexQuery
import com.algolia.search.model.response.ResponseSearch
import kotlinx.coroutines.runBlocking


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

    public val error = SubscriptionValue<Throwable?>(null)

    private val index = searcher.queries.indexOf(indexQuery)
    private var initialLoadSize: Int = 30

    init {
        if (index == -1) throw IllegalArgumentException("The IndexQuery is not present in SearcherMultipleIndex")
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, T>) {
        initialLoadSize = params.requestedLoadSize
        indexQuery.query.hitsPerPage = initialLoadSize
        indexQuery.query.page = 0
        runBlocking {
            try {
                val response = searcher.search().results[index]
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

        indexQuery.query.page = page
        indexQuery.query.hitsPerPage = params.requestedLoadSize
        runBlocking {
            try {
                val response = searcher.search().results[index]
                val nextKey = if (page + 1 < response.nbPages) params.key + 1 else null

                callback.onResult(response.hits.map(transformer), nextKey)
            } catch (throwable: Throwable) {
                error.value = throwable
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, T>) = Unit
}