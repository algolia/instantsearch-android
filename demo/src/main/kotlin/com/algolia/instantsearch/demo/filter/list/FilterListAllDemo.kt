package com.algolia.instantsearch.demo.filter.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.filter.list.FilterListViewModel
import com.algolia.instantsearch.helper.filter.list.connectFilterState
import com.algolia.instantsearch.helper.filter.list.connectView
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator
import kotlinx.android.synthetic.main.demo_filter_list.*
import kotlinx.android.synthetic.main.header_filter.*


class FilterListAllDemo : AppCompatActivity() {

    private val color = Attribute("color")
    private val price = Attribute("price")
    private val tags = "tags"
    private val all = "all"
    private val colors
        get() = mapOf(
            color.raw to ContextCompat.getColor(this, android.R.color.holo_red_dark),
            price.raw to ContextCompat.getColor(this, android.R.color.holo_blue_dark),
            tags to ContextCompat.getColor(this, android.R.color.holo_green_dark),
            all to ContextCompat.getColor(this, android.R.color.holo_purple)
        )
    private val index = client.initIndex(IndexName("stub"))
    private val allFilters = listOf(
        Filter.Numeric(price, 5..10),
        Filter.Tag("coupon"),
        Filter.Facet(color, "red"),
        Filter.Facet(color, "black"),
        Filter.Numeric(price, NumericOperator.Greater, 100)
    )

    private val groupIDAll = FilterGroupID.And(all)
    private val searcher = SearcherSingleIndex(index)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_filter_list)

        searcher.index = client.initIndex(intent.indexName)

        val viewModelAll = FilterListViewModel.All(allFilters)
        val viewAll = FilterListAdapter<Filter>()

        viewModelAll.connectFilterState(searcher.filterState, groupIDAll)
        viewModelAll.connectView(viewAll)

        configureRecyclerView(list, viewAll)

        onFilterChangedThenUpdateFiltersText(searcher.filterState, colors, filtersTextView)
        onClearAllThenClearFilters(searcher.filterState, filtersClearAll)
        onErrorThenUpdateFiltersText(searcher, filtersTextView)
        onResponseChangedThenUpdateNbHits(searcher, nbHits)
        configureToolbar()

        searcher.search()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
    }
}