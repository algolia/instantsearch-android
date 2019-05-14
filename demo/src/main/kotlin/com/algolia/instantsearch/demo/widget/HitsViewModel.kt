package com.algolia.instantsearch.demo.widget

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.algolia.instantsearch.helper.filter.state.Filters
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.response.ResponseSearch.Hit

class HitsViewModel(
    searcher: SearcherSingleIndex,
    filters: Filters,
    items: List<Hit> = listOf()
) : ViewModel() {
    private val dataSourceFactory = HitsDataSourceFactory(searcher, filters)
    private val config = PagedList.Config.Builder()
        .setPageSize(20)
        .setInitialLoadSizeHint(20) //FIXME: Leave default of 3*pageSize
        .build()

    var pagedHits: LiveData<PagedList<Hit>> =
        LivePagedListBuilder<Int, Hit>(dataSourceFactory, config)
            .build()
}