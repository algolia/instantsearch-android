package com.algolia.instantsearch.demo.filter.facet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.algolia.instantsearch.android.searcher.connectSearchView
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.filter.facet.*
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherForFacet

import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import kotlinx.android.synthetic.main.demo_home.*
import kotlinx.android.synthetic.main.header_filter.*


class FacetListSearchDemo : AppCompatActivity() {

    private val brand = Attribute("brand")
    private val index = client.initIndex(IndexName("mobile_demo_facet_list_search"))
    private val filterState = FilterState()
    private val searcher = SearcherForFacet(index, brand)
    private val colors
        get() = mapOf(
            brand.raw to ContextCompat.getColor(this, android.R.color.holo_red_dark)
        )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_facet_list_search)

        val hintIcon = ContextCompat.getDrawable(this, R.drawable.ic_search_hint)!!
        val hintText = getString(R.string.search_for_brands)

        val viewModel = FacetListViewModel(selectionMode = SelectionMode.Multiple)
        val view = FacetListAdapter()
        val presenter = FacetListPresenter(
            sortBy = listOf(FacetSortCriterion.IsRefined, FacetSortCriterion.CountDescending),
            limit = 100
        )

        viewModel.connectFilterState(brand, filterState)
        viewModel.connectSearcherFacet(searcher)
        viewModel.connectView(view, presenter)
        searcher.connectSearchView(searchView)
        configureRecyclerView(list, view)

        setSupportActionBar(toolbar)
        searchView.also {
            it.isSubmitButtonEnabled = false
            it.isFocusable = true
            it.setIconifiedByDefault(false)
            it.setOnQueryTextFocusChangeListener { _, hasFocus ->
                searchView.showQueryHintIcon(hasFocus, hintIcon, hintText)
            }
        }
        searcher.errorListeners += { throwable ->
            throwable.printStackTrace()
        }
        onChangeThenUpdateFiltersText(filterState, colors, filtersTextView)
        onClearAllThenClearFilters(filterState, filtersClearAll)

        searcher.search()
    }
}