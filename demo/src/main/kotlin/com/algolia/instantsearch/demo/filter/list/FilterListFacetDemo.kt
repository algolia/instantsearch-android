package com.algolia.instantsearch.demo.filter.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.core.selectable.list.connectView
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.filter.list.FilterListViewModel
import com.algolia.instantsearch.helper.filter.list.connectFilterState
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.groupAnd
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import kotlinx.android.synthetic.main.demo_filter_list.*
import kotlinx.android.synthetic.main.header_filter.*
import kotlinx.android.synthetic.main.include_list.*


class FilterListFacetDemo : AppCompatActivity() {

    private val color = Attribute("color")
    private val groupColor = groupAnd(color)
    private val filterState = FilterState()
    private val searcher = SearcherSingleIndex(stubIndex)
    private val facetFilters = listOf(
        Filter.Facet(color, "red"),
        Filter.Facet(color, "green"),
        Filter.Facet(color, "blue"),
        Filter.Facet(color, "yellow"),
        Filter.Facet(color, "black")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_filter_list)

        searcher.connectFilterState(filterState)

        val viewModelFacet = FilterListViewModel.Facet(facetFilters, selectionMode = SelectionMode.Single)
        val viewFacet = FilterListAdapter<Filter.Facet>()

        viewModelFacet.connectFilterState(filterState, groupColor)
        viewModelFacet.connectView(viewFacet)

        configureToolbar(toolbar)
        configureSearcher(searcher)
        configureRecyclerView(listTopLeft, viewFacet)
        onFilterChangedThenUpdateFiltersText(filterState, filtersTextView, color)
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