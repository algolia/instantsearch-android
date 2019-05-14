package com.algolia.instantsearch.demo.hits.paging

import androidx.paging.PageKeyedDataSource
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.response.ResponseSearch.Hit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HitsDataSource(
    private val coroutineScope: CoroutineScope,
    private val searcher: SearcherSingleIndex
) : PageKeyedDataSource<Int, Hit>() {

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Hit>) {
        searcher.query.hitsPerPage = params.requestedLoadSize
        searcher.query.page = 10
        coroutineScope.launch(Dispatchers.Default) {
            searcher.search().join()
            searcher.response?.let {
                callback.onResult(it.hits, null, searcher.query.page)
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Hit>) {
        searcher.query.page = params.key
        searcher.query.hitsPerPage = params.requestedLoadSize
        coroutineScope.launch(Dispatchers.Default) {
            searcher.search().join()
            searcher.response?.let {
                callback.onResult(it.hits, params.key + 1)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Hit>) {
        searcher.query.page = params.key
        searcher.query.hitsPerPage = params.requestedLoadSize
        coroutineScope.launch(Dispatchers.Default) {
            searcher.search().join()
            searcher.response?.let {
                callback.onResult(it.hits, params.key - 1)
            }
        }
    }
}