package com.algolia.instantsearch.demo.widget

import androidx.paging.PageKeyedDataSource
import com.algolia.instantsearch.helper.filter.state.Filters
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.response.ResponseSearch.Hit

class HitsDataSource(val searcher: SearcherSingleIndex, val filters: Filters) : PageKeyedDataSource<Int, Hit>() {
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Hit>) {
        searcher.query.hitsPerPage = params.requestedLoadSize
        searcher.query.page = 0
        searcher.onResponseChanged += {
            callback.onResult(it.hits, null, 1)
        }
        searcher.search(filters)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Hit>) {
        searcher.query.page = params.key
        searcher.query.hitsPerPage = params.requestedLoadSize
        searcher.onResponseChanged += {
            callback.onResult(it.hits, it.page + 1)
        }
        searcher.search(filters)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Hit>) {
        searcher.query.page = params.key
        searcher.query.hitsPerPage = params.requestedLoadSize
        searcher.onResponseChanged += {
            callback.onResult(it.hits, it.page - 1)
        }
        searcher.search(filters)
    }
}