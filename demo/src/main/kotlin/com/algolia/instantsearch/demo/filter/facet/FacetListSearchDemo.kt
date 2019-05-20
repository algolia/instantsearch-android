package com.algolia.instantsearch.demo.filter.facet

import android.os.Bundle
import androidx.core.content.ContextCompat
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.android.searchbox.connectView
import com.algolia.instantsearch.helper.filter.facet.*
import com.algolia.instantsearch.helper.searchbox.connectSearcher
import com.algolia.instantsearch.helper.searcher.SearcherForFacet
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import kotlinx.android.synthetic.main.demo_home.*
import kotlinx.android.synthetic.main.header_filter.*


class FacetListSearchDemo : DemoActivity() {

    private val brand = Attribute("brand")
    private val colors
        get() = mapOf(
            brand.raw to ContextCompat.getColor(this, android.R.color.holo_red_dark)
        )
    private val searcherForFacet = SearcherForFacet(client.initIndex(IndexName("stub")), brand)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_facet_list_search)
        setSupportActionBar(toolbar)

        searcherForFacet.index = client.initIndex(intent.indexName)

        val viewModel = FacetListViewModel(selectionMode = SelectionMode.Multiple)
        val view = FacetListAdapter()
        val presenter = FacetListPresenter(
            sortBy = listOf(FacetSortCriterion.IsRefined, FacetSortCriterion.CountDescending),
            limit = 100
        )

        viewModel.connectFilterState(brand, searcher.filterState)
        viewModel.connectSearcherForFacet(searcherForFacet)
        viewModel.connectView(view, presenter)
        val searchBoxViewModel = SearchBoxViewModel()
        searchBoxViewModel.connectView(searchView)
        searchBoxViewModel.connectSearcher(searcherForFacet)
        configureRecyclerView(list, view)
        configureSearchView(searchView)
        onFilterChangedThenUpdateFiltersText(searcher.filterState, colors, filtersTextView)
        onClearAllThenClearFilters(searcher.filterState, filtersClearAll)
        onResponseChangedThenUpdateNbHits(searcher, nbHits)

        searcher.search()
        searcherForFacet.search()
    }
}