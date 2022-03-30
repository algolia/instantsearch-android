package com.algolia.instantsearch.showcase.filter.range

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.number.range.Range
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.filter.range.FilterRangeConnector
import com.algolia.instantsearch.filter.range.connectView
import com.algolia.instantsearch.filter.state.FilterGroupID
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.filter.state.filters
import com.algolia.instantsearch.searcher.connectFilterState
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.*
import com.algolia.instantsearch.showcase.databinding.HeaderFilterBinding
import com.algolia.instantsearch.showcase.databinding.ShowcaseFilterRangeBinding
import com.algolia.search.model.Attribute

class FilterRangeShowcase : AppCompatActivity() {

    private val searcher = HitsSearcher(client, stubIndexName)
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
    private val range =
        FilterRangeConnector(filterState, price, range = initialRange, bounds = primaryBounds)
    private val connection = ConnectionHandler(
        range,
        searcher.connectFilterState(filterState, Debouncer(100))
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ShowcaseFilterRangeBinding.inflate(layoutInflater)
        val headerBinding = HeaderFilterBinding.bind(binding.headerFilter.root)
        setContentView(binding.root)

        connection += range.connectView(RangeSliderView(binding.slider))
        connection += range.connectView(RangeTextView(binding.rangeLabel))
        connection += range.connectView(BoundsTextView(binding.boundsLabel))

        binding.buttonChangeBounds.setOnClickListener {
            range.viewModel.bounds.value = Range(secondaryBounds)
            it.isEnabled = false
            binding.buttonResetBounds.isEnabled = true
        }
        binding.buttonResetBounds.setOnClickListener {
            range.viewModel.bounds.value = Range(primaryBounds)
            it.isEnabled = false
            binding.buttonChangeBounds.isEnabled = true
        }

        binding.reset.setOnClickListener {
            filterState.notify { set(filters) }
        }
        configureToolbar(binding.toolbar)
        configureSearcher(searcher)
        onFilterChangedThenUpdateFiltersText(filterState, headerBinding.filtersTextView, price)
        onClearAllThenClearFilters(filterState, headerBinding.filtersClearAll, connection)
        onErrorThenUpdateFiltersText(searcher, headerBinding.filtersTextView)
        onResponseChangedThenUpdateNbHits(searcher, headerBinding.nbHits, connection)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}
