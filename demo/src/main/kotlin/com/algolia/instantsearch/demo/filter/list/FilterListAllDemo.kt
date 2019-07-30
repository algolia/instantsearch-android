package com.algolia.instantsearch.demo.filter.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.filter.list.FilterListConnector
import com.algolia.instantsearch.helper.filter.list.connectView
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.groupAnd
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator
import kotlinx.android.synthetic.main.demo_filter_list.*
import kotlinx.android.synthetic.main.header_filter.*
import kotlinx.android.synthetic.main.include_list.*


class FilterListAllDemo : AppCompatActivity() {

    private val color = Attribute("color")
    private val price = Attribute("price")
    private val tags = Attribute("tags")
    private val all = Attribute("all")
    private val groupAll = groupAnd(all)
    private val filterState = FilterState()
    private val searcher = SearcherSingleIndex(stubIndex)
    private val filters = listOf(
        Filter.Numeric(price, 5..10),
        Filter.Tag("coupon"),
        Filter.Facet(color, "red"),
        Filter.Facet(color, "black"),
        Filter.Numeric(price, NumericOperator.Greater, 100)
    )
    private val filterList = FilterListConnector.All(filters, filterState, groupID = groupAll)
    private val connection = ConnectionHandler(filterList, searcher.connectFilterState(filterState))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_filter_list)

        val viewAll = FilterListAdapter<Filter>()

        connection += filterList.connectView(viewAll)

        configureToolbar(toolbar)
        configureSearcher(searcher)
        configureRecyclerView(listTopLeft, viewAll)
        onFilterChangedThenUpdateFiltersText(filterState, filtersTextView, color, price, tags, all)
        onClearAllThenClearFilters(filterState, filtersClearAll, connection)
        onErrorThenUpdateFiltersText(searcher, filtersTextView)
        onResponseChangedThenUpdateNbHits(searcher, nbHits, connection)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.disconnect()
    }
}