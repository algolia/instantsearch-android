package com.algolia.instantsearch.demo.filter.range

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.number.range.Range
import com.algolia.instantsearch.core.number.range.connectView
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.filter.range.FilterRangeViewModel
import com.algolia.instantsearch.helper.filter.range.connectFilterState
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import kotlinx.android.synthetic.main.demo_filter_range.*
import kotlinx.android.synthetic.main.header_filter.*


class FilterRangeDemo : AppCompatActivity() {

    private val searcher = SearcherSingleIndex(stubIndex)
    private val filterState = FilterState()
    private val price = Attribute("price")
    private val groupID = FilterGroupID(price)
    private val filters: Map<FilterGroupID, Set<Filter>> = mapOf(groupID to setOf(Filter.Numeric(price, 1..9)))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_filter_range)

        searcher.connectFilterState(filterState)

        val initialRange = Range(0..10)
        val viewModel = FilterRangeViewModel(initialRange)
        val sliderViewA = RangeSliderView(sliderA)
        val sliderViewB = RangeSliderView(sliderB)
        val rangeTextView = RangeTextView(rangeLabel)
        val boundsTextView = BoundsTextView(boundsLabel)

        viewModel.connectView(sliderViewA)
        viewModel.connectView(sliderViewB)
        viewModel.connectView(rangeTextView)
        viewModel.connectView(boundsTextView)
        viewModel.connectFilterState(price, filterState)

        buttonChangeBounds.setOnClickListener {
            viewModel.bounds.set(Range(0..20))
            it.isEnabled = false
            buttonResetBounds.isEnabled = true
        }
        buttonResetBounds.setOnClickListener {
            viewModel.bounds.set(initialRange)
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
}

