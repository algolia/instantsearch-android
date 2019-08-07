package com.algolia.instantsearch.demo.filter.facet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.android.filter.facet.FacetListAdapter
import com.algolia.instantsearch.helper.filter.facet.FacetListConnector
import com.algolia.instantsearch.helper.filter.facet.connectView
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import kotlinx.android.synthetic.main.demo_facet_list_persistent.*
import kotlinx.android.synthetic.main.header_filter.*
import kotlinx.android.synthetic.main.include_list.*
import kotlinx.android.synthetic.main.include_search.*


class FacetListPersistentDemo : AppCompatActivity() {

    private val color = Attribute("color")
    private val category = Attribute("category")
    private val index = client.initIndex(IndexName("stub"))
    private val filterState = FilterState()
    private val searcher = SearcherSingleIndex(index)
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
        setContentView(R.layout.demo_facet_list_persistent)

        val adapterColor = FacetListAdapter(FacetListViewHolderImpl.Factory)
        val adapterCategory = FacetListAdapter(FacetListViewHolderImpl.Factory)

        connection += facetListColor.connectView(adapterColor)
        connection += facetListCategory.connectView(adapterCategory)

        configureToolbar(toolbar)
        configureSearcher(searcher)
        configureSearchBox(searchView, searcher, connection)
        configureSearchView(searchView, getString(R.string.search_items))
        configureRecyclerView(listTopLeft, adapterColor)
        configureRecyclerView(listTopRight, adapterCategory)
        configureTitle(titleTopLeft, getString(R.string.multiple_choice))
        configureTitle(titleTopRight, getString(R.string.single_choice))
        onFilterChangedThenUpdateFiltersText(filterState, filtersTextView, color, category)
        onClearAllThenClearFilters(filterState, filtersClearAll, connection)
        onErrorThenUpdateFiltersText(searcher, filtersTextView)
        onResponseChangedThenUpdateNbHits(searcher, nbHits, connection)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.disconnect()
    }
}