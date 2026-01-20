package com.algolia.instantsearch.examples.android.showcase.androidview.filter.facet.dynamic

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.android.filter.facet.dynamic.DynamicFacetListAdapter
import com.algolia.instantsearch.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.filter.facet.dynamic.DynamicFacetListConnector
import com.algolia.instantsearch.filter.facet.dynamic.connectView
import com.algolia.instantsearch.filter.state.FilterOperator
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.examples.android.R
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searchbox.connectView
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.examples.android.showcase.androidview.configureRecyclerView
import com.algolia.instantsearch.examples.android.showcase.androidview.configureSearchView
import com.algolia.instantsearch.examples.android.showcase.androidview.configureToolbar
import com.algolia.instantsearch.examples.android.databinding.IncludeSearchBinding
import com.algolia.instantsearch.examples.android.databinding.ShowcaseDynamicFacetListBinding
import com.algolia.search.client.ClientSearch
import com.algolia.search.logging.LogLevel
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName

class DynamicFacetShowcase : AppCompatActivity() {

    val client = ClientSearch(
        "RVURKQXRHU",
        "937e4e6ec422ff69fe89b569dba30180",
        LogLevel.All
    )
    val searcher = HitsSearcher(client, "test_facet_ordering")
    val filterState = FilterState()
    val searchBox = SearchBoxConnector(searcher)
    val color = "color"
    val country = "country"
    val brand = "brand"
    val size = "size"
    val dynamicFacets = DynamicFacetListConnector(
        searcher = searcher,
        filterState = filterState,
        selectionModeForAttribute = mapOf(
            color to SelectionMode.Multiple,
            country to SelectionMode.Multiple
        ),
        filterGroupForAttribute = mapOf(
            brand to (brand to FilterOperator.Or),
            color to (color to FilterOperator.Or),
            size to (color to FilterOperator.Or),
            country to (color to FilterOperator.Or),
        )
    )
    private val connection = ConnectionHandler(searchBox, dynamicFacets)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ShowcaseDynamicFacetListBinding.inflate(layoutInflater)
        val searchBinding = IncludeSearchBinding.bind(binding.searchBox.root)
        setContentView(binding.root)

        val searchBoxView = SearchBoxViewAppCompat(searchBinding.searchView)
        connection += searchBox.connectView(searchBoxView)

        val factory = ViewHolderFactory()
        val adapter = DynamicFacetListAdapter(factory)
        connection += dynamicFacets.connectView(adapter)

        configureToolbar(binding.toolbar)
        configureSearchView(searchBinding.searchView, getString(R.string.search_brands))
        configureRecyclerView(binding.hits, adapter)

        searcher.query = searcher.query.copy(facets = listOf(brand, color, size, country))
        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}
