package com.algolia.instantsearch.demo.guide

import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.demo.filter.facet.FacetListViewHolderImpl
import com.algolia.instantsearch.helper.android.filter.connectPagedList
import com.algolia.instantsearch.helper.android.filter.facet.FacetListAdapter
import com.algolia.instantsearch.helper.android.list.SearcherSingleIndexDataSource
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxConnectorPagedList
import com.algolia.instantsearch.helper.filter.facet.FacetListConnector
import com.algolia.instantsearch.helper.filter.facet.connectView
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.instantsearch.helper.stats.StatsConnector
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import io.ktor.client.features.logging.LogLevel


class SampleViewModel : ViewModel() {

    val client = ClientSearch(ApplicationID("latency"), APIKey("1f6fd3a6fb973cb08419fe7d288fa4db"), LogLevel.ALL)
    val index = client.initIndex(IndexName("bestbuy_promo"))
    val searcher = SearcherSingleIndex(index)

    val category = Attribute("category")
    val filterState = FilterState()

    val dataSourceFactory = SearcherSingleIndexDataSource.Factory(searcher, Product.serializer())
    val pagedListConfig = PagedList.Config.Builder().setPageSize(50).build()
    val products = LivePagedListBuilder(dataSourceFactory, pagedListConfig).build()

    val searchBox = SearchBoxConnectorPagedList(searcher, listOf(products))
    val facetList = FacetListConnector(searcher, filterState, category)
    val stats = StatsConnector(searcher)

    val adapterFacet = FacetListAdapter(FacetListViewHolderImpl.Factory)
    val adapterProduct = ProductAdapter()

    val connection = ConnectionHandler(
        searchBox,
        facetList,
        stats,
        searcher.connectFilterState(filterState),
        facetList.connectView(adapterFacet),
        filterState.connectPagedList(products)
    )

    override fun onCleared() {
        super.onCleared()
        searcher.cancel()
        connection.disconnect()
    }
}