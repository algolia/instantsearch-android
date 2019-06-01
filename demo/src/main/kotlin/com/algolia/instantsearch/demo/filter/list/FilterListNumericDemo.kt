package com.algolia.instantsearch.demo.filter.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.filter.list.FilterListViewModel
import com.algolia.instantsearch.helper.filter.list.connectFilterState
import com.algolia.instantsearch.helper.filter.list.connectView
import com.algolia.instantsearch.helper.filter.state.groupAnd
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator
import kotlinx.android.synthetic.main.demo_filter_list.*
import kotlinx.android.synthetic.main.header_filter.*
import kotlinx.android.synthetic.main.include_list.*


class FilterListNumericDemo : AppCompatActivity() {

    private val price = Attribute("price")
    private val colors
        get() = mapOf(price.raw to ContextCompat.getColor(this, android.R.color.holo_blue_dark))
    private val numericFilters = listOf(
        Filter.Numeric(price, NumericOperator.Less, 5),
        Filter.Numeric(price, 5..10),
        Filter.Numeric(price, 10..25),
        Filter.Numeric(price, 25..100),
        Filter.Numeric(price, NumericOperator.Greater, 100)
    )

    private val groupPrice = groupAnd(price)
    private val searcher = SearcherSingleIndex(stubIndex)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_filter_list)

        val viewModelNumeric = FilterListViewModel.Numeric(numericFilters)
        val viewNumeric = FilterListAdapter<Filter.Numeric>()

        viewModelNumeric.connectFilterState(searcher.filterState, groupPrice)
        viewModelNumeric.connectView(viewNumeric)

        configureToolbar(toolbar)
        configureSearcher(searcher)
        configureRecyclerView(listTopLeft, viewNumeric)
        onFilterChangedThenUpdateFiltersText(searcher.filterState, colors, filtersTextView)
        onClearAllThenClearFilters(searcher.filterState, filtersClearAll)
        onErrorThenUpdateFiltersText(searcher, filtersTextView)
        onResponseChangedThenUpdateNbHits(searcher, nbHits)

        searcher.search()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
    }
}