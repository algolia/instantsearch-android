package com.algolia.instantsearch.demo.filter.facet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.filter.facet.FacetListViewModel
import com.algolia.instantsearch.helper.filter.facet.connectFilterState
import com.algolia.instantsearch.helper.filter.facet.connectSearcher
import com.algolia.instantsearch.helper.filter.facet.connectView
import com.algolia.instantsearch.helper.searchbox.connectSearcher
import com.algolia.instantsearch.helper.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import kotlinx.android.synthetic.main.demo_facet_list_persistent.*
import kotlinx.android.synthetic.main.header_filter.*
import kotlinx.android.synthetic.main.include_list.*
import kotlinx.android.synthetic.main.include_search.*


class FacetListPersistentDemo : AppCompatActivity() {

    private val color = Attribute("color")
    private val category = Attribute("category")
    private val colors
        get() = mapOf(
            color.raw to ContextCompat.getColor(this, android.R.color.holo_red_dark),
            category.raw to ContextCompat.getColor(this, android.R.color.holo_blue_dark)
        )
    private val index = client.initIndex(IndexName("stub"))

    private val searcher = SearcherSingleIndex(index)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_facet_list_persistent)

        val colorViewModel = FacetListViewModel(persistentSelection = true)
        val colorAdapter = FacetListAdapter()

        colorViewModel.connectFilterState(color, searcher.filterState)
        colorViewModel.connectView(colorAdapter)
        colorViewModel.connectSearcher(color, searcher)

        val categoryViewModel = FacetListViewModel(selectionMode = SelectionMode.Single, persistentSelection = true)
        val categoryAdapter = FacetListAdapter()

        categoryViewModel.connectFilterState(category, searcher.filterState)
        categoryViewModel.connectView(categoryAdapter)
        categoryViewModel.connectSearcher(category, searcher)

        val searchBoxViewModel = SearchBoxViewModel()
        val searchBoxView = SearchBoxViewAppCompat(searchView)

        searchBoxViewModel.connectView(searchBoxView)
        searchBoxViewModel.connectSearcher(searcher)

        configureToolbar(toolbar)
        configureSearcher(searcher)
        configureSearchView(searchView, getString(R.string.search_items))
        configureRecyclerView(listTopLeft, colorAdapter)
        configureRecyclerView(listTopRight, categoryAdapter)
        configureTitle(titleTopLeft, getString(R.string.multiple_choice), colors.getValue(color.raw))
        configureTitle(titleTopRight, getString(R.string.single_choice), colors.getValue(category.raw))
        onFilterChangedThenUpdateFiltersText(searcher.filterState, colors, filtersTextView)
        onClearAllThenClearFilters(searcher.filterState, filtersClearAll)
        onErrorThenUpdateFiltersText(searcher, filtersTextView)
        onResponseChangedThenUpdateNbHits(searcher, nbHits)

        searcher.search()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
    }
}