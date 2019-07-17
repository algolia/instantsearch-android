package com.algolia.instantsearch.helper.android.list

import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.helper.deserialize
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.DeserializationStrategy


public class SearcherSingleIndexDataSource<T>(
    private val searcher: SearcherSingleIndex,
    private val deserializer: DeserializationStrategy<T>
) : PageKeyedDataSource<Int, T>() {

    public class Factory<T>(
        private val searcher: SearcherSingleIndex,
        private val deserializer: DeserializationStrategy<T>
    ) : DataSource.Factory<Int, T>() {

        public lateinit var lastDataSource: DataSource<Int, T>

        override fun create(): DataSource<Int, T> {
            lastDataSource = SearcherSingleIndexDataSource(searcher, deserializer)
            return lastDataSource
        }
    }

    private var initialLoadSize: Int = 30

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, T>) {
        initialLoadSize = params.requestedLoadSize
        searcher.query.hitsPerPage = initialLoadSize
        searcher.query.page = 0
        runBlocking {
            val response = searcher.search()
            val nextKey = if (response.nbHits > initialLoadSize) 1 else null

            callback.onResult(response.hits.deserialize(deserializer), 0, response.nbHits, null, nextKey)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        val initialOffset = (initialLoadSize / params.requestedLoadSize) - 1
        val page = params.key + initialOffset

        searcher.query.page = page
        searcher.query.hitsPerPage = params.requestedLoadSize
        runBlocking {
            val response = searcher.search()
            val nextKey = if (page + 1 < response.nbPages) params.key + 1 else null

            callback.onResult(response.hits.deserialize(deserializer), nextKey)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, T>) = Unit
}