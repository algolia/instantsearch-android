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

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, T>) {
        query.hitsPerPage = params.requestedLoadSize
        query.page = 0
        runBlocking {
            searcher.search()
            searcher.response?.let {
                callback.onResult(it.results[index].hits.deserialize(deserializer), null, 0)
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        query.page = params.key + (query.hitsPerPage!! / params.requestedLoadSize)
        query.hitsPerPage = params.requestedLoadSize
        runBlocking {
            searcher.search()
            searcher.response?.let {
                callback.onResult(it.results[index].hits.deserialize(deserializer), params.key + 1)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, T>) = Unit
}