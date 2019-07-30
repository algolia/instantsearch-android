package com.algolia.instantsearch.demo.filter.toggle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.android.filter.FilterToggleViewCompoundButton
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.toggle.FilterToggleConnector
import com.algolia.instantsearch.helper.filter.toggle.connectView
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator
import kotlinx.android.synthetic.main.demo_filter_toggle.*
import kotlinx.android.synthetic.main.header_filter.*


class FilterToggleDemo : AppCompatActivity() {

    private val promotions = Attribute("promotions")
    private val size = Attribute("size")
    private val tags = Attribute("_tags")
    private val filterState = FilterState()
    private val searcher = SearcherSingleIndex(stubIndex)
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
        setContentView(R.layout.demo_filter_toggle)

        val viewCoupon = FilterToggleViewCompoundButton(switchCoupon)
        val viewSize = FilterToggleViewCompoundButton(checkBoxSize)
        val viewVintage = FilterToggleViewCompoundButton(checkBoxVintage)

        connection += toggleCoupon.connectView(viewCoupon)
        connection += toggleSize.connectView(viewSize)
        connection += toggleVintage.connectView(viewVintage)

        configureToolbar(toolbar)
        configureSearcher(searcher)
        onFilterChangedThenUpdateFiltersText(filterState, filtersTextView, promotions, size, tags)
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


