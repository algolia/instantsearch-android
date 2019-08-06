package com.algolia.instantsearch.helper.android.list

import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.search.helper.deserialize
import com.algolia.search.model.multipleindex.IndexQuery
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.DeserializationStrategy
import java.lang.IllegalArgumentException


public class SearcherMultipleIndexDataSource<T>(
    private val searcher: SearcherMultipleIndex,
    private val indexQuery: IndexQuery,
    private val deserializer: DeserializationStrategy<T>
) : PageKeyedDataSource<Int, T>() {

    public class Factory<T>(
        private val searcher: SearcherMultipleIndex,
        private val indexQuery: IndexQuery,
        private val deserializer: DeserializationStrategy<T>
    ) : DataSource.Factory<Int, T>() {

        override fun create(): DataSource<Int, T> {
            return SearcherMultipleIndexDataSource(searcher, indexQuery, deserializer)
        }
    }

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
            val response = searcher.search().results[index]
            val nextKey = if (response.nbHits > initialLoadSize) 1 else null

            callback.onResult(response.hits.deserialize(deserializer), 0, response.nbHits, null, nextKey)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        val initialOffset = (initialLoadSize / params.requestedLoadSize) - 1
        val page = params.key + initialOffset

        indexQuery.query.page = page
        indexQuery.query.hitsPerPage = params.requestedLoadSize
        runBlocking {
            val response = searcher.search().results[index]
            val nextKey = if (page + 1 < response.nbPages) params.key + 1 else null

            callback.onResult(response.hits.deserialize(deserializer), nextKey)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, T>) = Unit
}