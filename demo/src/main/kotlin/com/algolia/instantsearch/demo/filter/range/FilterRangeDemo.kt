package com.algolia.instantsearch.demo.filter.range

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.number.range.Range
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.filter.range.FilterRangeWidget
import com.algolia.instantsearch.helper.filter.range.connectionView
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.filters
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connectionFilterState
import com.algolia.search.model.Attribute
import kotlinx.android.synthetic.main.demo_filter_range.*
import kotlinx.android.synthetic.main.header_filter.*


class FilterRangeDemo : AppCompatActivity() {

    private val searcher = SearcherSingleIndex(stubIndex)
    private val price = Attribute("price")
    private val groupID = FilterGroupID(price)
    private val primaryBounds = 0..15
    private val secondaryBounds = 0..10
    private val initialRange = 0..15
    private val filters = filters {
        group(groupID) {
            range(price, initialRange)
        }
    }
    private val filterState = FilterState(filters)
    private val widgetRange = FilterRangeWidget(filterState, price, range = initialRange, bounds = primaryBounds)
    private val connection = ConnectionHandler(widgetRange)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_filter_range)

        connection.apply {
            +searcher.connectionFilterState(filterState, Debouncer(100))
            +widgetRange.connectionView(RangeSliderView(slider))
            +widgetRange.connectionView(RangeTextView(rangeLabel))
            +widgetRange.connectionView(BoundsTextView(boundsLabel))
        }

        buttonChangeBounds.setOnClickListener {
            widgetRange.viewModel.bounds.value = Range(secondaryBounds)
            it.isEnabled = false
            buttonResetBounds.isEnabled = true
        }
        buttonResetBounds.setOnClickListener {
            widgetRange.viewModel.bounds.value = Range(primaryBounds)
            it.isEnabled = false
            buttonChangeBounds.isEnabled = true
        }

        reset.setOnClickListener {
            filterState.notify { set(filters) }
        }
        configureToolbar(toolbar)
        configureSearcher(searcher)
        onFilterChangedThenUpdateFiltersText(filterState, filtersTextView, price)
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

