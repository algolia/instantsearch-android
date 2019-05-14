package com.algolia.instantsearch.demo.widget

import androidx.paging.DataSource
import com.algolia.instantsearch.helper.filter.state.Filters
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.response.ResponseSearch

class HitsDataSourceFactory(
    private val searcher: SearcherSingleIndex,
    private val filters: Filters
) : DataSource.Factory<Int, ResponseSearch.Hit>() {
    override fun create(): DataSource<Int, ResponseSearch.Hit> = HitsDataSource(searcher, filters)
}