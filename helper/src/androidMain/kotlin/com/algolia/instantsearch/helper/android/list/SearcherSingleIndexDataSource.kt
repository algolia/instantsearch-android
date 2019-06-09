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

        override fun create(): DataSource<Int, T> {
            return SearcherSingleIndexDataSource(searcher, deserializer)
        }
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, T>) {
        searcher.query.hitsPerPage = params.requestedLoadSize
        searcher.query.page = 0
        runBlocking {
            val response = searcher.search()

            callback.onResult(response.hits.deserialize(deserializer), null, 0)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        searcher.query.page = params.key + (searcher.query.hitsPerPage!! / params.requestedLoadSize)
        searcher.query.hitsPerPage = params.requestedLoadSize
        runBlocking {
            val response = searcher.search()

            callback.onResult(response.hits.deserialize(deserializer), params.key + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, T>) = Unit
}