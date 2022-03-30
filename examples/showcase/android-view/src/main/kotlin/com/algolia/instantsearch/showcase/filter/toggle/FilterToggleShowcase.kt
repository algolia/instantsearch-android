package com.algolia.instantsearch.showcase.filter.toggle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.android.filter.toggle.FilterToggleViewCompoundButton
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.filter.toggle.FilterToggleConnector
import com.algolia.instantsearch.filter.toggle.connectView
import com.algolia.instantsearch.searcher.connectFilterState
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.*
import com.algolia.instantsearch.showcase.databinding.HeaderFilterBinding
import com.algolia.instantsearch.showcase.databinding.ShowcaseFilterToggleBinding
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator

class FilterToggleShowcase : AppCompatActivity() {

    private val promotions = Attribute("promotions")
    private val size = Attribute("size")
    private val tags = Attribute("_tags")
    private val filterState = FilterState()
    private val searcher = HitsSearcher(client, stubIndexName)
    private val filterCoupon = Filter.Facet(promotions, "coupon")
    private val filterSize = Filter.Numeric(size, NumericOperator.Greater, 40)
    private val filterVintage = Filter.Tag("vintage")
    private val toggleCoupon = FilterToggleConnector(filterState, filterCoupon)
    private val toggleSize = FilterToggleConnector(filterState, filterSize)
    private val toggleVintage = FilterToggleConnector(filterState, filterVintage)
    private val connection = ConnectionHandler(
        toggleCoupon,
        toggleSize,
        toggleVintage,
        searcher.connectFilterState(filterState)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ShowcaseFilterToggleBinding.inflate(layoutInflater)
        val headerBinding = HeaderFilterBinding.bind(binding.headerFilter.root)
        setContentView(binding.root)

        val viewCoupon = FilterToggleViewCompoundButton(binding.switchCoupon)
        val viewSize = FilterToggleViewCompoundButton(binding.checkBoxSize)
        val viewVintage = FilterToggleViewCompoundButton(binding.checkBoxVintage)

        connection += toggleCoupon.connectView(viewCoupon)
        connection += toggleSize.connectView(viewSize)
        connection += toggleVintage.connectView(viewVintage)

        configureToolbar(binding.toolbar)
        configureSearcher(searcher)
        onFilterChangedThenUpdateFiltersText(filterState, headerBinding.filtersTextView, promotions, size, tags)
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
