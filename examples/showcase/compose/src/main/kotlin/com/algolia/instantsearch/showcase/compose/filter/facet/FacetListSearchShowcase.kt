package com.algolia.instantsearch.showcase.compose.filter.facet

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.algolia.instantsearch.compose.filter.facet.FacetListState
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.filter.facet.FacetListConnector
import com.algolia.instantsearch.filter.facet.FacetListPresenterImpl
import com.algolia.instantsearch.filter.facet.FacetSortCriterion
import com.algolia.instantsearch.filter.facet.connectView
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searchbox.connectView
import com.algolia.instantsearch.searcher.connectFilterState
import com.algolia.instantsearch.searcher.facets.FacetsSearcher
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.compose.*
import com.algolia.instantsearch.showcase.compose.ui.ShowcaseTheme
import com.algolia.instantsearch.showcase.compose.ui.component.*
import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Query

class FacetListSearchShowcase : AppCompatActivity() {

    private val brand = Attribute("brand")
    private val searcher = HitsSearcher(client, stubIndexName)
    private val searcherForFacet = FacetsSearcher(client, stubIndexName, brand, Query(maxFacetHits = 15))
    private val filterState = FilterState()

    private val searchBoxState = SearchBoxState()
    private val searchBox = SearchBoxConnector(searcherForFacet)

    private val facetListState = FacetListState()
    private val facetPresenter = FacetListPresenterImpl(
        sortBy = listOf(FacetSortCriterion.IsRefined, FacetSortCriterion.CountDescending),
        limit = 100
    )
    private val facetList = FacetListConnector(
        searcher = searcherForFacet,
        filterState = filterState,
        attribute = brand,
        selectionMode = SelectionMode.Multiple
    )
    private val filterHeader = HeaderFilterConnector(
        searcher = searcher,
        filterState = filterState,
        filterColors = filterColors(brand)
    )

    private val connections = ConnectionHandler(searchBox, facetList, filterHeader)

    init {
        connections += searcher.connectFilterState(filterState)
        connections += searchBox.connectView(searchBoxState)
        connections += facetList.connectView(facetListState, facetPresenter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowcaseTheme {
                FilterScreenScreen()
            }
        }

        configureSearcher(searcher)
        configureSearcher(searcherForFacet)

        searcher.searchAsync()
        searcherForFacet.searchAsync()
    }

    @Composable
    fun FilterScreenScreen() {
        Scaffold(
            topBar = {
                SearchTopBar(
                    searchBoxState = searchBoxState,
                    onBackPressed = ::onBackPressed
                )
            },
            content = {
                Column(Modifier.fillMaxWidth()) {
                    HeaderFilter(
                        modifier = Modifier.padding(16.dp),
                        filterHeader = filterHeader
                    )
                    FacetList(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        facetListState = facetListState
                    )
                }
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        searcherForFacet.cancel()
        connections.clear()
    }
}
