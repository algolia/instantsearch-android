package com.algolia.instantsearch.demo.filter.facet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.android.searchbox.connectSearchView
import com.algolia.instantsearch.helper.filter.facet.*
import com.algolia.instantsearch.helper.searchbox.connectSearcher
import com.algolia.instantsearch.helper.searcher.SearcherForFacet
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.Attribute
import kotlinx.android.synthetic.main.demo_home.*
import kotlinx.android.synthetic.main.header_filter.*


class FacetListSearchDemo : AppCompatActivity() {

    private val brand = Attribute("brand")
    private val colors
        get() = mapOf(
            brand.raw to ContextCompat.getColor(this, android.R.color.holo_red_dark)
        )
    private val searcher = SearcherSingleIndex(stubIndex)
    private val searcherForFacet = SearcherForFacet(stubIndex, brand)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_facet_list_search)
        setSupportActionBar(toolbar)

        val index = client.initIndex(intent.indexName)

        searcher.index = index
        searcherForFacet.index = index

        val facetViewModel = FacetListViewModel(selectionMode = SelectionMode.Multiple)
        val facetView = FacetListAdapter()
        val facetPresenter = FacetListPresenter(
            sortBy = listOf(FacetSortCriterion.IsRefined, FacetSortCriterion.CountDescending),
            limit = 100
        )

        facetViewModel.connectFilterState(brand, searcher.filterState)
        facetViewModel.connectSearcherForFacet(searcherForFacet)
        facetViewModel.connectView(facetView, facetPresenter)

        val searchBoxViewModel = SearchBoxViewModel()

        searchBoxViewModel.connectSearchView(searchView)
        searchBoxViewModel.connectSearcher(searcherForFacet)

        configureRecyclerView(list, facetView)
        configureSearchView(searchView)
        onFilterChangedThenUpdateFiltersText(searcher.filterState, colors, filtersTextView)
        onClearAllThenClearFilters(searcher.filterState, filtersClearAll)
        onResponseChangedThenUpdateNbHits(searcher, nbHits)

        searcher.search()
        searcherForFacet.search()
    }
}