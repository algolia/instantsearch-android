package com.algolia.instantsearch.guides.compose

import androidx.lifecycle.ViewModel
import com.algolia.instantsearch.android.paging3.Paginator
import com.algolia.instantsearch.android.paging3.facet.connectPaginator
import com.algolia.instantsearch.android.paging3.searchbox.connectPaginator
import com.algolia.instantsearch.compose.filter.facet.FacetListState
import com.algolia.instantsearch.compose.item.StatsTextState
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.filter.facet.FacetListConnector
import com.algolia.instantsearch.filter.facet.connectView
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.guides.model.Product
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searchbox.connectView
import com.algolia.instantsearch.searcher.connectFilterState
import com.algolia.instantsearch.searcher.facets.FacetsSearcher
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.stats.StatsConnector
import com.algolia.instantsearch.stats.StatsPresenterImpl
import com.algolia.instantsearch.stats.connectView
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import io.ktor.client.features.logging.*


class MainViewModel : ViewModel() {

    val client = ClientSearch(
        ApplicationID("latency"),
        APIKey("1f6fd3a6fb973cb08419fe7d288fa4db"),
        LogLevel.ALL
    )
    val indexName = IndexName("instant_search")
    val searcher = HitsSearcher(client, indexName)

    // Search Box
    val searchBoxState = SearchBoxState()
    val searchBoxConnector = SearchBoxConnector(searcher)

    // Hits
    val hitsPaginator = Paginator(searcher) { it.deserialize(Product.serializer()) }

    // Stats
    val statsText = StatsTextState()
    val statsConnector = StatsConnector(searcher)

    // Filters
    val facetList = FacetListState()
    val filterState = FilterState()
    val categories = Attribute("categories")
    val searcherForFacet = FacetsSearcher(client, indexName, categories)
    val facetListConnector = FacetListConnector(
        searcher = searcherForFacet,
        filterState = filterState,
        attribute = categories,
        selectionMode = SelectionMode.Multiple
    )

    val connections = ConnectionHandler(searchBoxConnector, statsConnector, facetListConnector)

    init {
        connections += searchBoxConnector.connectView(searchBoxState)
        connections += statsConnector.connectView(statsText, StatsPresenterImpl())
        connections += searcher.connectFilterState(filterState)
        connections += facetListConnector.connectView(facetList)
        connections += facetListConnector.connectPaginator(hitsPaginator)
        connections += searchBoxConnector.connectPaginator(hitsPaginator)

        searcherForFacet.searchAsync()
    }

    override fun onCleared() {
        super.onCleared()
        searcher.cancel()
        connections.clear()
        searcherForFacet.cancel()
    }
}
