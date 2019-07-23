package com.algolia.instantsearch.demo.filter.range

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.Connections
import com.algolia.instantsearch.core.connection.connect
import com.algolia.instantsearch.core.connection.disconnect
import com.algolia.instantsearch.core.number.range.Range
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.filter.range.FilterRangeWidget
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.filters
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.search.model.Attribute
import kotlinx.android.synthetic.main.demo_filter_range.*
import kotlinx.android.synthetic.main.header_filter.*


class FilterRangeDemo : AppCompatActivity() {

    private val searcher = SearcherSingleIndex(stubIndex)
    private val price = Attribute("price")
    private val groupID = FilterGroupID(price)
    private val initialBounds = 0..20
    private val extendedBounds = 0..40
    private val initialRange = 0..20
    private val filters = filters {
        group(groupID) {
            range(price, initialRange)
        }
    }
    private val filterState = FilterState(filters)
    private val widget = FilterRangeWidget(filterState, price, range = initialRange, bounds = initialBounds)
    private lateinit var connections: Connections

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_filter_range)

        searcher.connectFilterState(filterState)

        val sliderViewA = RangeSliderView(sliderA)
        val sliderViewB = RangeSliderView(sliderB)
        val rangeTextView = RangeTextView(rangeLabel)
        val boundsTextView = BoundsTextView(boundsLabel)

        widget.connect()
        connections = widget.with(sliderViewA, sliderViewB, rangeTextView, boundsTextView).connect()

        buttonChangeBounds.setOnClickListener {
            widget.viewModel.bounds.value = Range(extendedBounds)
            it.isEnabled = false
            buttonResetBounds.isEnabled = true
        }
        buttonResetBounds.setOnClickListener {
            widget.viewModel.bounds.value = Range(initialBounds)
            it.isEnabled = false
            buttonChangeBounds.isEnabled = true
        }

        reset.setOnClickListener {
            filterState.notify { set(filters) }
        }
        configureToolbar(toolbar)
        configureSearcher(searcher)
        onFilterChangedThenUpdateFiltersText(filterState, filtersTextView, price)
        onClearAllThenClearFilters(filterState, filtersClearAll)
        onErrorThenUpdateFiltersText(searcher, filtersTextView)
        onResponseChangedThenUpdateNbHits(searcher, nbHits)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        widget.disconnect()
        connections.disconnect()
    }
}

