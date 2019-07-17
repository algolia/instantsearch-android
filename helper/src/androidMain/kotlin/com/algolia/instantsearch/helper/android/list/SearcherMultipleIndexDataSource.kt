package com.algolia.instantsearch.helper.android.list

import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.search.helper.deserialize
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.DeserializationStrategy


public class SearcherMultipleIndexDataSource<T>(
    private val searcher: SearcherMultipleIndex,
    private val index: Int,
    private val deserializer: DeserializationStrategy<T>
) : PageKeyedDataSource<Int, T>() {

    public class Factory<T>(
        private val searcher: SearcherMultipleIndex,
        private val index: Int,
        private val deserializer: DeserializationStrategy<T>
    ) : DataSource.Factory<Int, T>() {

        override fun create(): DataSource<Int, T> {
            return SearcherMultipleIndexDataSource(searcher, index, deserializer)
        }
    }

    private val query = searcher.queries[index].query
    private var initialLoadSize: Int = 30

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, T>) {
        initialLoadSize = params.requestedLoadSize
        query.hitsPerPage = initialLoadSize
        query.page = 0
        runBlocking {
            val response = searcher.search().results[index]
            val nextKey = if (response.nbHits > initialLoadSize) 1 else null

            callback.onResult(response.hits.deserialize(deserializer), 0, response.nbHits, null, nextKey)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        val initialOffset = (initialLoadSize / params.requestedLoadSize) - 1
        val page = params.key + initialOffset

        query.page = page
        query.hitsPerPage = params.requestedLoadSize
        runBlocking {
            val response = searcher.search().results[index]
            val nextKey = if (page + 1 < response.nbPages) params.key + 1 else null

            callback.onResult(response.hits.deserialize(deserializer), nextKey)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, T>) = Unit
}