package com.algolia.instantsearch.demo.selectable.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.algolia.instantsearch.demo.*
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator
import filter.FilterGroupID
import kotlinx.android.synthetic.main.selectable_header.*
import kotlinx.android.synthetic.main.selectable_numerics_demo.*
import searcher.SearcherSingleIndex
import selectable.list.SelectableFilterListViewModel
import selectable.list.SelectionMode
import selectable.list.connectSearcher
import selectable.list.connectView


class SelectableFilterListDemo : AppCompatActivity() {

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
    private val index = client.initIndex(IndexName("mobile_demo_selectable_filter_list"))
    private val searcher = SearcherSingleIndex(index)
    private val groupIDColor = FilterGroupID.And(color)
    private val groupIDPrice = FilterGroupID.And(price)
    private val groupIDTags = FilterGroupID.Or(tags)
    private val groupIDAll = FilterGroupID.And(all)
    private val facetFilters =  listOf(
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selectable_numerics_demo)

        val viewModelFacet = SelectableFilterListViewModel.Facet(facetFilters, selectionMode = SelectionMode.Single)
        val viewFacet = SelectableFilterListAdapter<Filter.Facet>()

        viewModelFacet.connectSearcher(searcher, groupIDColor)
        viewModelFacet.connectView(viewFacet)

        val viewModelNumeric = SelectableFilterListViewModel.Numeric(numericFilters)
        val viewNumeric = SelectableFilterListAdapter<Filter.Numeric>()

        viewModelNumeric.connectSearcher(searcher, groupIDPrice)
        viewModelNumeric.connectView(viewNumeric)

        val viewModelTag = SelectableFilterListViewModel.Tag(tagFilters)
        val viewTag = SelectableFilterListAdapter<Filter.Tag>()

        viewModelTag.connectSearcher(searcher, groupIDTags)
        viewModelTag.connectView(viewTag)

        val viewModelAll = SelectableFilterListViewModel.All(allFilters)
        val viewAll = SelectableFilterListAdapter<Filter>()

        viewModelAll.connectSearcher(searcher, groupIDAll)
        viewModelAll.connectView(viewAll)

        configureRecyclerView(listTopLeft, viewFacet)
        configureRecyclerView(listTopRight, viewNumeric)
        configureRecyclerView(listBottomLeft, viewTag)
        configureRecyclerView(listBottomRight, viewAll)

        onChangeThenUpdateFiltersText(searcher, colors, filtersTextView)
        onErrorThenUpdateFiltersText(searcher, filtersTextView)
        onClearAllThenClearFilters(searcher, filtersClearAll)
        onResponseChangedThenUpdateStats(searcher)
    }
}