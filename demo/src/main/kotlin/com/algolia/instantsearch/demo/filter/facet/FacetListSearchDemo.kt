package com.algolia.instantsearch.demo.filter.facet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.filter.facet.*
import com.algolia.instantsearch.helper.searchbox.connectSearcher
import com.algolia.instantsearch.helper.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.SearcherForFacets
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.Attribute
import kotlinx.android.synthetic.main.demo_facet_list_search.*
import kotlinx.android.synthetic.main.header_filter.*
import kotlinx.android.synthetic.main.include_search.*


class FacetListSearchDemo : AppCompatActivity() {

    private val brand = Attribute("brand")
    private val colors
        get() = mapOf(
            brand.raw to ContextCompat.getColor(this, android.R.color.holo_red_dark)
        )
    private val searcher = SearcherSingleIndex(stubIndex)
    private val searcherForFacet = SearcherForFacets(stubIndex, brand)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_facet_list_search)

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
        val searchBoxView = SearchBoxViewAppCompat(searchView)

        searchBoxViewModel.connectView(searchBoxView)
        searchBoxViewModel.connectSearcher(searcherForFacet)

        configureToolbar(toolbar)
        configureRecyclerView(list, facetView)
        configureSearchView(searchView, getString(R.string.search_brands))
        onFilterChangedThenUpdateFiltersText(searcher.filterState, colors, filtersTextView)
        onClearAllThenClearFilters(searcher.filterState, filtersClearAll)
        onResponseChangedThenUpdateNbHits(searcher, nbHits)

        searcher.search()
        searcherForFacet.search()
    }
}