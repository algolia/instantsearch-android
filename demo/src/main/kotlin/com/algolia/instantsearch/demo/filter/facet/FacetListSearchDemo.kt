package com.algolia.instantsearch.demo.filter.facet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.algolia.instantsearch.helper.android.searcher.connectSearchView
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.filter.facet.*
import com.algolia.instantsearch.helper.searcher.SearcherForFacet
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import kotlinx.android.synthetic.main.demo_home.*
import kotlinx.android.synthetic.main.header_filter.*


class FacetListSearchDemo : AppCompatActivity() {

    private val brand = Attribute("brand")
    private val index = client.initIndex(IndexName("mobile_demo_facet_list_search"))
    private val colors
        get() = mapOf(
            brand.raw to ContextCompat.getColor(this, android.R.color.holo_red_dark)
        )
    private val searcher = SearcherSingleIndex(index)
    private val searcherForFacet = SearcherForFacet(index, brand)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_facet_list_search)
        setSupportActionBar(toolbar)

        val viewModel = FacetListViewModel(selectionMode = SelectionMode.Multiple)
        val view = FacetListAdapter()
        val presenter = FacetListPresenter(
            sortBy = listOf(FacetSortCriterion.IsRefined, FacetSortCriterion.CountDescending),
            limit = 100
        )

        viewModel.connectFilterState(brand, searcher.filterState)
        viewModel.connectSearcherForFacet(searcherForFacet)
        viewModel.connectView(view, presenter)
        searcherForFacet.connectSearchView(searchView)
        configureRecyclerView(list, view)
        configureSearchView(searchView)
        searcherForFacet.onErrorChanged += { throwable ->
            throwable.printStackTrace()
        }
        onChangeThenUpdateFiltersText(searcher.filterState, colors, filtersTextView)
        onClearAllThenClearFilters(searcher.filterState, filtersClearAll)
        onResponseChangedThenUpdateNbHits(searcher)

        searcher.search()
        searcherForFacet.search()
    }
}