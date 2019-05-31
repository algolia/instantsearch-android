package com.algolia.instantsearch.demo.filter.clear

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.android.filter.clear.FilterClearViewImpl
import com.algolia.instantsearch.helper.filter.clear.FilterClearViewModel
import com.algolia.instantsearch.helper.filter.clear.connectFilterState
import com.algolia.instantsearch.helper.filter.clear.connectView
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.clear
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import kotlinx.android.synthetic.main.demo_filter_clear.*
import kotlinx.android.synthetic.main.demo_filter_toggle_default.toolbar
import kotlinx.android.synthetic.main.header_filter.*


class FilterClearDemo : AppCompatActivity() {

    private val color = Attribute("color")
    private val category = Attribute("category")
    private val colorID = FilterGroupID.Or(color)
    private val categoryID = FilterGroupID.Or(category)
    private val colorFacets = setOf(Filter.Facet(color, "red"), Filter.Facet(color, "green"))
    private val categoryFacets = setOf(Filter.Facet(category, "shoe"))

    private val colors
        get() = mapOf(
            color.raw to ContextCompat.getColor(this, android.R.color.holo_red_dark),
            category.raw to ContextCompat.getColor(this, android.R.color.holo_green_dark)
        )

    private val initialFacetGroups: MutableMap<FilterGroupID, Set<Filter.Facet>> =
        mutableMapOf(colorID to colorFacets, categoryID to categoryFacets)
    private val searcher = SearcherSingleIndex(stubIndex, filterState = FilterState(facetGroups = initialFacetGroups))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_filter_clear)

        val viewModel = FilterClearViewModel()
        viewModel.connectView(FilterClearViewImpl(filtersClearAll))
        viewModel.connectFilterState(searcher.filterState)

        val viewModelClearColors = FilterClearViewModel()
        val viewClearColors = FilterClearViewImpl(buttonClearColors)
        viewModelClearColors.connectView(viewClearColors)
        viewModelClearColors.connectFilterState(searcher.filterState, colorID)

        val viewModelClearExceptColors = FilterClearViewModel()
        val viewClearOther = FilterClearViewImpl(buttonClearExceptColors)
        viewModelClearExceptColors.connectView(viewClearOther)
        viewModelClearExceptColors.connectFilterState(searcher.filterState, true, colorID)

        buttonReset.setOnClickListener {
            searcher.filterState.notify {
                //TODO: worth implementing filterState.reset(MutableFiltersImpl)
                clear()
                add(colorID, *colorFacets.toTypedArray())
                add(categoryID, *categoryFacets.toTypedArray())
            }
        }
        configureToolbar(toolbar)
        configureSearcher(searcher)
        onFilterChangedThenUpdateFiltersText(searcher.filterState, colors, filtersTextView)
        onErrorThenUpdateFiltersText(searcher, filtersTextView)
        onResponseChangedThenUpdateNbHits(searcher, nbHits)

        searcher.search()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
    }
}