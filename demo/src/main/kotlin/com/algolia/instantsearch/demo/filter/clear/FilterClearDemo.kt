package com.algolia.instantsearch.demo.filter.clear

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.android.filter.clear.FilterClearViewImpl
import com.algolia.instantsearch.helper.filter.clear.ClearMode
import com.algolia.instantsearch.helper.filter.clear.FilterClearViewModel
import com.algolia.instantsearch.helper.filter.clear.connectFilterState
import com.algolia.instantsearch.helper.filter.clear.connectView
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import kotlinx.android.synthetic.main.demo_filter_clear.*
import kotlinx.android.synthetic.main.demo_filter_toggle_default.toolbar
import kotlinx.android.synthetic.main.header_filter.*


class FilterClearDemo : AppCompatActivity() {

    private val color = Attribute("color")
    private val category = Attribute("category")
    private val groupIDColor = FilterGroupID.Or(color)
    private val groupIDCategory = FilterGroupID.Or(category)
    private val filterColors = setOf(Filter.Facet(color, "red"), Filter.Facet(color, "green"))
    private val filterCategories = setOf(Filter.Facet(category, "shoe"))

    private val colors
        get() = mapOf(
            color.raw to ContextCompat.getColor(this, android.R.color.holo_red_dark),
            category.raw to ContextCompat.getColor(this, android.R.color.holo_green_dark)
        )

    private val initialFacetGroups: MutableMap<FilterGroupID, Set<Filter.Facet>> = mutableMapOf(
        groupIDColor to filterColors,
        groupIDCategory to filterCategories
    )
    private val searcher = SearcherSingleIndex(stubIndex, filterState = FilterState(facetGroups = initialFacetGroups))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_filter_clear)

        val viewModel = FilterClearViewModel()

        viewModel.connectView(FilterClearViewImpl(filtersClearAll))
        viewModel.connectFilterState(searcher.filterState)

        val clearColorsViewModel = FilterClearViewModel()
        val clearColorsView = FilterClearViewImpl(buttonClearSpecified)

        clearColorsViewModel.connectView(clearColorsView)
        clearColorsViewModel.connectFilterState(searcher.filterState, listOf(groupIDColor), ClearMode.Specified)

        val clearExceptColorsViewModel = FilterClearViewModel()
        val clearExceptColorsView = FilterClearViewImpl(buttonClearExcept)

        clearExceptColorsViewModel.connectView(clearExceptColorsView)
        clearExceptColorsViewModel.connectFilterState(searcher.filterState, listOf(groupIDColor), ClearMode.Except)

        reset.setOnClickListener {
            searcher.filterState.notify {
                //TODO: worth implementing filterState.reset(MutableFiltersImpl)
                clear()
                add(groupIDColor, *filterColors.toTypedArray())
                add(groupIDCategory, *filterCategories.toTypedArray())
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