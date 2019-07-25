package com.algolia.instantsearch.demo.filter.facet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.filter.facet.FacetListPresenterImpl
import com.algolia.instantsearch.helper.filter.facet.FacetListWidget
import com.algolia.instantsearch.helper.filter.facet.FacetSortCriterion
import com.algolia.instantsearch.helper.filter.facet.connectionView
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searchbox.SearchBoxWidget
import com.algolia.instantsearch.helper.searchbox.connectionView
import com.algolia.instantsearch.helper.searcher.SearcherForFacets
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.search.model.Attribute
import kotlinx.android.synthetic.main.demo_facet_list_search.*
import kotlinx.android.synthetic.main.header_filter.*
import kotlinx.android.synthetic.main.include_search.*


class FacetListSearchDemo : AppCompatActivity() {

    private val brand = Attribute("brand")
    private val filterState = FilterState()
    private val searcher = SearcherSingleIndex(stubIndex)
    private val searcherForFacet = SearcherForFacets(stubIndex, brand)
    private val searchBox = SearchBoxWidget(searcher)
    private val widgetFacetList = FacetListWidget(
        searcher = searcherForFacet,
        filterState = filterState,
        attribute = brand,
        selectionMode = SelectionMode.Multiple
    )
    private val connection = ConnectionHandler(searchBox, widgetFacetList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_facet_list_search)

        val index = client.initIndex(intent.indexName)
        val searchBoxView = SearchBoxViewAppCompat(searchView)
        val facetView = FacetListAdapter()
        val facetPresenter = FacetListPresenterImpl(
            sortBy = listOf(FacetSortCriterion.IsRefined, FacetSortCriterion.CountDescending),
            limit = 100
        )

        searcher.index = index
        searcherForFacet.index = index

        connection.apply {
            +searcher.connectFilterState(filterState)
            +widgetFacetList.connectionView(facetView, facetPresenter)
            +searchBox.connectionView(searchBoxView)
        }

        configureToolbar(toolbar)
        configureRecyclerView(list, facetView)
        configureSearchView(searchView, getString(R.string.search_brands))
        onFilterChangedThenUpdateFiltersText(filterState, filtersTextView, brand)
        onClearAllThenClearFilters(filterState, filtersClearAll, connection)
        onResponseChangedThenUpdateNbHits(searcher, nbHits)

        searcher.searchAsync()
        searcherForFacet.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        searcherForFacet.cancel()
        connection.disconnect()
    }
}