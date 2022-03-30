package com.algolia.instantsearch.showcase.filter.facet.dynamic

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
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searchbox.connectView
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.R
import com.algolia.instantsearch.showcase.configureRecyclerView
import com.algolia.instantsearch.showcase.configureSearchView
import com.algolia.instantsearch.showcase.configureToolbar
import com.algolia.instantsearch.showcase.databinding.IncludeSearchBinding
import com.algolia.instantsearch.showcase.databinding.ShowcaseDynamicFacetListBinding
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import io.ktor.client.features.logging.*

class DynamicFacetShowcase : AppCompatActivity() {

    val client = ClientSearch(
        ApplicationID("RVURKQXRHU"),
        APIKey("937e4e6ec422ff69fe89b569dba30180"),
        LogLevel.ALL
    )
    val searcher = HitsSearcher(client, IndexName("test_facet_ordering"))
    val filterState = FilterState()
    val searchBox = SearchBoxConnector(searcher)
    val color = Attribute("color")
    val country = Attribute("country")
    val brand = Attribute("brand")
    val size = Attribute("size")
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

        searcher.query.facets = setOf(brand, color, size, country)
        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}
