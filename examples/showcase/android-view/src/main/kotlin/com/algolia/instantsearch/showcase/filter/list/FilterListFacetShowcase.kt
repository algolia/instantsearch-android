package com.algolia.instantsearch.showcase.filter.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.filter.list.FilterListConnector
import com.algolia.instantsearch.filter.list.connectView
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.filter.state.groupAnd
import com.algolia.instantsearch.searcher.connectFilterState
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.*
import com.algolia.instantsearch.showcase.databinding.HeaderFilterBinding
import com.algolia.instantsearch.showcase.databinding.IncludeListBinding
import com.algolia.instantsearch.showcase.databinding.ShowcaseFilterListBinding
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter

class FilterListFacetShowcase : AppCompatActivity() {

    private val color = Attribute("color")
    private val groupColor = groupAnd(color)
    private val filterState = FilterState()
    private val searcher = HitsSearcher(client, stubIndexName)
    private val facetFilters = listOf(
        Filter.Facet(color, "red"),
        Filter.Facet(color, "green"),
        Filter.Facet(color, "blue"),
        Filter.Facet(color, "yellow"),
        Filter.Facet(color, "black")
    )
    private val filterList = FilterListConnector.Facet(
        filters = facetFilters,
        filterState = filterState,
        selectionMode = SelectionMode.Single,
        groupID = groupColor
    )
    private val connection = ConnectionHandler(filterList, searcher.connectFilterState(filterState))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ShowcaseFilterListBinding.inflate(layoutInflater)
        val listBinding = IncludeListBinding.bind(binding.list.root)
        val headerBinding = HeaderFilterBinding.bind(listBinding.headerFilter.root)
        setContentView(binding.root)

        val viewFacet = FilterListAdapter<Filter.Facet>()

        connection += filterList.connectView(viewFacet)

        configureToolbar(binding.toolbar)
        configureSearcher(searcher)
        configureRecyclerView(listBinding.listTopLeft, viewFacet)
        onFilterChangedThenUpdateFiltersText(filterState, headerBinding.filtersTextView, color)
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
