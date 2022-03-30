package com.algolia.instantsearch.showcase.filter.facet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.showcase.*
import com.algolia.instantsearch.android.filter.facet.FacetListAdapter
import com.algolia.instantsearch.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.filter.facet.FacetListConnector
import com.algolia.instantsearch.filter.facet.FacetListPresenterImpl
import com.algolia.instantsearch.filter.facet.FacetSortCriterion
import com.algolia.instantsearch.filter.facet.connectView
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searchbox.connectView
import com.algolia.instantsearch.searcher.connectFilterState
import com.algolia.instantsearch.searcher.facets.FacetsSearcher
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.databinding.HeaderFilterBinding
import com.algolia.instantsearch.showcase.databinding.IncludeSearchBinding
import com.algolia.instantsearch.showcase.databinding.ShowcaseFacetListSearchBinding
import com.algolia.search.model.Attribute

class FacetListSearchShowcase : AppCompatActivity() {

    private val brand = Attribute("brand")
    private val filterState = FilterState()
    private val searcher =  HitsSearcher(client, stubIndexName)
    private val searcherForFacet = FacetsSearcher(client, stubIndexName, brand)
    private val searchBox = SearchBoxConnector(searcherForFacet)
    private val facetList = FacetListConnector(
        searcher = searcherForFacet,
        filterState = filterState,
        attribute = brand,
        selectionMode = SelectionMode.Multiple
    )
    private val connection = ConnectionHandler(
        searchBox,
        facetList,
        searcher.connectFilterState(filterState)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ShowcaseFacetListSearchBinding.inflate(layoutInflater)
        val searchBinding = IncludeSearchBinding.bind(binding.searchBox.root)
        val headerBinding = HeaderFilterBinding.bind(binding.headerFilter.root)
        setContentView(binding.root)

        val searchBoxView = SearchBoxViewAppCompat(searchBinding.searchView)
        val facetView = FacetListAdapter(FacetListViewHolderImpl.Factory)
        val facetPresenter = FacetListPresenterImpl(
            sortBy = listOf(FacetSortCriterion.IsRefined, FacetSortCriterion.CountDescending),
            limit = 100
        )

        configureSearcher(searcher)
        configureSearcher(searcherForFacet)

        connection += facetList.connectView(facetView, facetPresenter)
        connection += searchBox.connectView(searchBoxView)

        configureToolbar(binding.toolbar)
        configureRecyclerView(binding.hits, facetView)
        configureSearchView(searchBinding.searchView, getString(R.string.search_brands))
        onFilterChangedThenUpdateFiltersText(filterState, headerBinding.filtersTextView, brand)
        onClearAllThenClearFilters(filterState, headerBinding.filtersClearAll, connection)
        onResponseChangedThenUpdateNbHits(searcher, headerBinding.nbHits, connection)

        searcher.searchAsync()
        searcherForFacet.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        searcherForFacet.cancel()
        connection.clear()
    }
}
