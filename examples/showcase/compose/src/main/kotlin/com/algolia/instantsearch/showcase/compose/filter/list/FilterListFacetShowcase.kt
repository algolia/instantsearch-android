package com.algolia.instantsearch.showcase.compose.filter.list

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.compose.filter.list.FilterListState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.filter.list.FilterListConnector
import com.algolia.instantsearch.filter.list.connectView
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.filter.state.groupAnd
import com.algolia.instantsearch.searcher.connectFilterState
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.compose.client
import com.algolia.instantsearch.showcase.compose.configureSearcher
import com.algolia.instantsearch.showcase.compose.filterColors
import com.algolia.instantsearch.showcase.compose.stubIndexName
import com.algolia.instantsearch.showcase.compose.ui.component.HeaderFilterConnector
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter


class FilterListFacetShowcase : AppCompatActivity() {

    private val color = Attribute("color")
    private val groupColor = groupAnd(color)
    private val filterState = FilterState()
    private val searcher = HitsSearcher(client, stubIndexName)
    private val facetFilters = listOf(
        Filter.Facet(color, "red"),
        Filter.Facet(color, "green"),
        Filter.Facet(color, "blue"),
        Filter.Facet(color, "yellow"),
        Filter.Facet(color, "black")
    )

    private val filterListState = FilterListState<Filter.Facet>()
    private val filterList = FilterListConnector.Facet(
        filters = facetFilters,
        filterState = filterState,
        selectionMode = SelectionMode.Single,
        groupID = groupColor
    )

    private val filterHeader = HeaderFilterConnector(
        searcher = searcher,
        filterState = filterState,
        filterColors = filterColors(color)
    )

    private val connection = ConnectionHandler(filterList, filterHeader)

    init {
        connection += searcher.connectFilterState(filterState)
        connection += filterList.connectView(filterListState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FilterListScreen(filterHeader = filterHeader, filterListState = filterListState)
        }

        configureSearcher(searcher)
        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}