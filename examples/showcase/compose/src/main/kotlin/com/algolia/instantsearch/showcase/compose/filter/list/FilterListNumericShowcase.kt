package com.algolia.instantsearch.showcase.compose.filter.list

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.compose.filter.list.FilterListState
import com.algolia.instantsearch.core.connection.ConnectionHandler
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
import com.algolia.search.model.filter.NumericOperator


class FilterListNumericShowcase : AppCompatActivity() {

    private val price = Attribute("price")
    private val groupPrice = groupAnd(price)
    private val searcher = HitsSearcher(client, stubIndexName)
    private val filterState = FilterState()
    private val filters = listOf(
        Filter.Numeric(price, NumericOperator.Less, 5),
        Filter.Numeric(price, 5..10),
        Filter.Numeric(price, 10..25),
        Filter.Numeric(price, 25..100),
        Filter.Numeric(price, NumericOperator.Greater, 100)
    )

    private val filterListState = FilterListState<Filter.Numeric>()
    private val filterList = FilterListConnector.Numeric(filters, filterState, groupID = groupPrice)

    private val filterHeader = HeaderFilterConnector(
        searcher = searcher,
        filterState = filterState,
        filterColors = filterColors(price)
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