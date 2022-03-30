package com.algolia.instantsearch.showcase.filter.facet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.android.filter.facet.FacetListAdapter
import com.algolia.instantsearch.filter.facet.FacetListConnector
import com.algolia.instantsearch.filter.facet.connectView
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.searcher.connectFilterState
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.*
import com.algolia.instantsearch.showcase.databinding.HeaderFilterBinding
import com.algolia.instantsearch.showcase.databinding.IncludeListBinding
import com.algolia.instantsearch.showcase.databinding.IncludeSearchBinding
import com.algolia.instantsearch.showcase.databinding.ShowcaseFacetListPersistentBinding
import com.algolia.search.model.Attribute

class FacetListPersistentShowcase : AppCompatActivity() {

    private val color = Attribute("color")
    private val category = Attribute("category")
    private val filterState = FilterState()
    private val searcher = HitsSearcher(client, stubIndexName)
    private val facetListColor = FacetListConnector(
        searcher = searcher,
        filterState = filterState,
        attribute = color,
        selectionMode = SelectionMode.Multiple,
        persistentSelection = true
    )
    private val facetListCategory = FacetListConnector(
        searcher = searcher,
        filterState = filterState,
        attribute = category,
        selectionMode = SelectionMode.Single,
        persistentSelection = true
    )
    private val connection = ConnectionHandler(
        facetListColor,
        facetListCategory,
        searcher.connectFilterState(filterState)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ShowcaseFacetListPersistentBinding.inflate(layoutInflater)
        val searchBinding = IncludeSearchBinding.bind(binding.searchBox.root)
        val listBinding = IncludeListBinding.bind(binding.list.root)
        val headerBinding = HeaderFilterBinding.bind(listBinding.headerFilter.root)
        setContentView(binding.root)

        val adapterColor = FacetListAdapter(FacetListViewHolderImpl.Factory)
        val adapterCategory = FacetListAdapter(FacetListViewHolderImpl.Factory)

        connection += facetListColor.connectView(adapterColor)
        connection += facetListCategory.connectView(adapterCategory)

        configureToolbar(binding.toolbar)
        configureSearcher(searcher)
        configureSearchBox(searchBinding.searchView, searcher, connection)
        configureSearchView(searchBinding.searchView, getString(R.string.search_items))
        configureRecyclerView(listBinding.listTopLeft, adapterColor)
        configureRecyclerView(listBinding.listTopRight, adapterCategory)
        configureTitle(listBinding.titleTopLeft, getString(R.string.multiple_choice))
        configureTitle(listBinding.titleTopRight, getString(R.string.single_choice))
        onFilterChangedThenUpdateFiltersText(filterState, headerBinding.filtersTextView, color, category)
        onClearAllThenClearFilters(filterState, headerBinding.filtersClearAll, connection)
        onErrorThenUpdateFiltersText(searcher, headerBinding.filtersTextView)
        onResponseChangedThenUpdateNbHits(searcher, headerBinding.nbHits, connection)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}