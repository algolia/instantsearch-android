package com.algolia.instantsearch.demo.filter.current

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.android.filter.FilterCurrentViewImpl
import com.algolia.instantsearch.helper.filter.current.FilterCurrentWidget
import com.algolia.instantsearch.helper.filter.current.connectView
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.filters
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.NumericOperator
import kotlinx.android.synthetic.main.demo_filter_current.*
import kotlinx.android.synthetic.main.header_filter.*


class FilterCurrentDemo : AppCompatActivity() {

    private val color = Attribute("color")
    private val price = Attribute("price")
    private val tags = Attribute("_tags")
    private val groupColor = FilterGroupID(color)
    private val groupPrice = FilterGroupID(price)
    private val groupTags = FilterGroupID(tags)
    private val filters = filters {
        group(groupColor) {
            facet(color, "red")
            facet(color, "green")
        }
        group(groupTags) {
            tag("mobile")
        }
        group(groupPrice) {
            comparison(price, NumericOperator.NotEquals, 42)
            range(price, IntRange(0, 100))
        }
    }
    private val filterState = FilterState(filters)
    private val searcher = SearcherSingleIndex(stubIndex)
    private val widgetCurrentFiltersColor = FilterCurrentWidget(filterState, listOf(groupColor))
    private val widgetCurrentFiltersAll = FilterCurrentWidget(filterState)
    private val connection = ConnectionHandler(widgetCurrentFiltersColor, widgetCurrentFiltersAll)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_filter_current)

        connection.apply {
            +searcher.connectFilterState(filterState)
            +widgetCurrentFiltersAll.connectView(FilterCurrentViewImpl(chipGroupAll, R.layout.filter_chip))
            +widgetCurrentFiltersColor.connectView(FilterCurrentViewImpl(chipGroupColors, R.layout.filter_chip))
        }


        configureSearcher(searcher)
        configureToolbar(toolbar)
        onFilterChangedThenUpdateFiltersText(filterState, filtersTextView, color, price, tags)
        onErrorThenUpdateFiltersText(searcher, filtersTextView)
        onResponseChangedThenUpdateNbHits(searcher, nbHits, connection)
        onClearAllThenClearFilters(filterState, filtersClearAll, connection)
        onResetThenRestoreFilters(reset, filterState, filters)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.disconnect()
    }
}