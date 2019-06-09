package com.algolia.instantsearch.demo.filter.toggle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.android.filter.FilterToggleViewCompoundButton
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.toggle.FilterToggleViewModel
import com.algolia.instantsearch.helper.filter.toggle.connectFilterState
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_filter_toggle)

        searcher.connectFilterState(filterState)

        val viewModelCoupon = FilterToggleViewModel(Filter.Facet(promotions, "coupon"))
        val viewCoupon = FilterToggleViewCompoundButton(switchCoupon)

        viewModelCoupon.connectFilterState(filterState)
        viewModelCoupon.connectView(viewCoupon)

        val viewModelSize = FilterToggleViewModel(Filter.Numeric(size, NumericOperator.Greater, 40))
        val viewSize = FilterToggleViewCompoundButton(checkBoxSize)

        viewModelSize.connectFilterState(filterState)
        viewModelSize.connectView(viewSize)

        val viewModelVintage = FilterToggleViewModel(Filter.Tag("vintage"))
        val viewVintage =
            FilterToggleViewCompoundButton(checkBoxVintage)

        viewModelVintage.connectFilterState(filterState)
        viewModelVintage.connectView(viewVintage)

        configureToolbar(toolbar)
        configureSearcher(searcher)
        onFilterChangedThenUpdateFiltersText(filterState, filtersTextView, promotions, size, tags)
        onClearAllThenClearFilters(filterState, filtersClearAll)
        onErrorThenUpdateFiltersText(searcher, filtersTextView)
        onResponseChangedThenUpdateNbHits(searcher, nbHits)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
    }
}


