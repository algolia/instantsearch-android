package com.algolia.instantsearch.showcase.compose.filter.list

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.compose.filter.list.FilterListState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.filter.list.FilterListConnector
import com.algolia.instantsearch.filter.list.connectView
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.filter.state.groupOr
import com.algolia.instantsearch.searcher.connectFilterState
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.compose.client
import com.algolia.instantsearch.showcase.compose.configureSearcher
import com.algolia.instantsearch.showcase.compose.filterColors
import com.algolia.instantsearch.showcase.compose.stubIndexName
import com.algolia.instantsearch.showcase.compose.ui.component.HeaderFilterConnector
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter


class FilterListTagShowcase : AppCompatActivity() {

    private val tags = Attribute("_tags")
    private val groupTags = groupOr(tags)
    private val filterState = FilterState()
    private val searcher = HitsSearcher(client, stubIndexName)
    private val filters = listOf(
        Filter.Tag("free shipping"),
        Filter.Tag("coupon"),
        Filter.Tag("free return"),
        Filter.Tag("on sale"),
        Filter.Tag("no exchange")
    )

    private val filterListState = FilterListState<Filter.Tag>()
    private val filterList = FilterListConnector.Tag(filters, filterState, groupID = groupTags)

    private val filterHeader = HeaderFilterConnector(
        searcher = searcher,
        filterState = filterState,
        filterColors = filterColors(tags)
    )

    private val connections = ConnectionHandler(filterList, filterHeader)

    init {
        connections += searcher.connectFilterState(filterState)
        connections += filterList.connectView(filterListState)
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
        connections.clear()
    }
}