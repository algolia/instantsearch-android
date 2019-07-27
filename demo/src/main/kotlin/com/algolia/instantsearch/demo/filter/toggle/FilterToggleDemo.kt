package com.algolia.instantsearch.demo.filter.toggle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.android.filter.FilterToggleViewCompoundButton
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.toggle.FilterToggleWidget
import com.algolia.instantsearch.helper.filter.toggle.connectionView
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connectionFilterState
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
    private val widgetCoupon = FilterToggleWidget(filterState, filterCoupon)
    private val widgetSize = FilterToggleWidget(filterState, filterSize)
    private val widgetVintage = FilterToggleWidget(filterState, filterVintage)
    private val connection = ConnectionHandler(widgetCoupon, widgetSize, widgetVintage)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_filter_toggle)

        val viewCoupon = FilterToggleViewCompoundButton(switchCoupon)
        val viewSize = FilterToggleViewCompoundButton(checkBoxSize)
        val viewVintage = FilterToggleViewCompoundButton(checkBoxVintage)

        connection.apply {
            +searcher.connectionFilterState(filterState)
            +widgetCoupon.connectionView(viewCoupon)
            +widgetSize.connectionView(viewSize)
            +widgetVintage.connectionView(viewVintage)
        }

        configureToolbar(toolbar)
        configureSearcher(searcher)
        onFilterChangedThenUpdateFiltersText(filterState, filtersTextView, promotions, size, tags)
        onClearAllThenClearFilters(filterState, filtersClearAll, connection)
        onErrorThenUpdateFiltersText(searcher, filtersTextView)
        onResponseChangedThenUpdateNbHits(searcher, nbHits)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.disconnect()
    }
}


