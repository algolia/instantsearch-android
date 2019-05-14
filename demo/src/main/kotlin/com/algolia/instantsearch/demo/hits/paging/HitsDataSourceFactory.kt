package com.algolia.instantsearch.demo.hits.paging

import androidx.paging.DataSource
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.response.ResponseSearch
import kotlinx.coroutines.CoroutineScope


class HitsDataSourceFactory(
    private val coroutineScope: CoroutineScope,
    private val searcher: SearcherSingleIndex
) : DataSource.Factory<Int, ResponseSearch.Hit>() {

    override fun create(): DataSource<Int, ResponseSearch.Hit> {
        return HitsDataSource(coroutineScope, searcher)
    }
}