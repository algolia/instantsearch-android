package com.algolia.instantsearch.demo.guide

import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.demo.list.movie.Movie
import com.algolia.instantsearch.helper.android.list.SearcherSingleIndexDataSource
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxConnectorPagedList
import com.algolia.instantsearch.helper.searchbox.SearchBoxConnector
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName


class SampleViewModel : ViewModel() {

    private val client = ClientSearch(ApplicationID("latency"), APIKey("1f6fd3a6fb973cb08419fe7d288fa4db"))
    private val index = client.initIndex(IndexName("bestbuy_promo"))

    public val searcher = SearcherSingleIndex(index)

    private val dataSourceFactory = SearcherSingleIndexDataSource.Factory(searcher, Product.serializer())
    private val pagedListConfig = PagedList.Config.Builder().setPageSize(50).build()

    public val products = LivePagedListBuilder(dataSourceFactory, pagedListConfig).build()
    public val searchBox = SearchBoxConnectorPagedList(searcher, listOf(products))

    private val connection = ConnectionHandler(searchBox)

    override fun onCleared() {
        super.onCleared()
        searcher.cancel()
        connection.disconnect()
    }
}