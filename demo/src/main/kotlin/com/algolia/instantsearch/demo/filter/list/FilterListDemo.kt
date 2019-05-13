package com.algolia.instantsearch.demo.filter.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.filter.list.FilterListViewModel
import com.algolia.instantsearch.helper.filter.list.connectFilterState
import com.algolia.instantsearch.helper.filter.list.connectView
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connect
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator
import kotlinx.android.synthetic.main.demo_filter_list.*
import kotlinx.android.synthetic.main.header_filter.*


class FilterListDemo : AppCompatActivity() {

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
    private val index = client.initIndex(IndexName("mobile_demo_filter_list"))
    private val facetFilters = listOf(
        Filter.Facet(color, "red"),
        Filter.Facet(color, "green"),
        Filter.Facet(color, "blue"),
        Filter.Facet(color, "yellow"),
        Filter.Facet(color, "black")
    )
    private val numericFilters = listOf(
        Filter.Numeric(price, NumericOperator.Less, 5),
        Filter.Numeric(price, 5..10),
        Filter.Numeric(price, 10..25),
        Filter.Numeric(price, 25..100),
        Filter.Numeric(price, NumericOperator.Greater, 100)
    )
    private val tagFilters = listOf(
        Filter.Tag("free shipping"),
        Filter.Tag("coupon"),
        Filter.Tag("free return"),
        Filter.Tag("on sale"),
        Filter.Tag("no exchange")
    )
    private val allFilters = listOf(
        Filter.Numeric(price, 5..10),
        Filter.Tag("coupon"),
        Filter.Facet(color, "red"),
        Filter.Facet(color, "black"),
        Filter.Numeric(price, NumericOperator.Greater, 100)
    )

    private val groupIDColor = FilterGroupID.And(color)
    private val groupIDPrice = FilterGroupID.And(price)
    private val groupIDTags = FilterGroupID.Or(tags)
    private val groupIDAll = FilterGroupID.And(all)
    private val searcher = SearcherSingleIndex(index)
    private val filterState = FilterState().connect(searcher)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_filter_list)

        val viewModelFacet = FilterListViewModel.Facet(facetFilters, selectionMode = SelectionMode.Single)
        val viewFacet = FilterListAdapter<Filter.Facet>()

        viewModelFacet.connectFilterState(filterState, groupIDColor)
        viewModelFacet.connectView(viewFacet)

        val viewModelNumeric = FilterListViewModel.Numeric(numericFilters)
        val viewNumeric = FilterListAdapter<Filter.Numeric>()

        viewModelNumeric.connectFilterState(filterState, groupIDPrice)
        viewModelNumeric.connectView(viewNumeric)

        val viewModelTag = FilterListViewModel.Tag(tagFilters)
        val viewTag = FilterListAdapter<Filter.Tag>()

        viewModelTag.connectFilterState(filterState, groupIDTags)
        viewModelTag.connectView(viewTag)

        val viewModelAll = FilterListViewModel.All(allFilters)
        val viewAll = FilterListAdapter<Filter>()

        viewModelAll.connectFilterState(filterState, groupIDAll)
        viewModelAll.connectView(viewAll)

        configureRecyclerView(listTopLeft, viewFacet)
        configureRecyclerView(listTopRight, viewNumeric)
        configureRecyclerView(listBottomLeft, viewTag)
        configureRecyclerView(listBottomRight, viewAll)

        onChangeThenUpdateFiltersText(filterState, colors, filtersTextView)
        onClearAllThenClearFilters(filterState, filtersClearAll)
        onErrorThenUpdateFiltersText(searcher, filtersTextView)
        onResponseChangedThenUpdateStats(searcher)

        searcher.search(filterState)
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
    }
}