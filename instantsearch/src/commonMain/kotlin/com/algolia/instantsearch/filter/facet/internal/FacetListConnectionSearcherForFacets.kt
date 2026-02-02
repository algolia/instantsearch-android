package com.algolia.instantsearch.filter.facet.internal

import com.algolia.client.model.search.FacetHits
import com.algolia.client.model.search.SearchForFacetValuesResponse
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.AbstractConnection
import com.algolia.instantsearch.filter.facet.FacetListViewModel
import com.algolia.instantsearch.searcher.SearcherForFacets

internal data class FacetListConnectionSearcherForFacets(
    private val viewModel: FacetListViewModel,
    private val searcher: SearcherForFacets<*>,
) : AbstractConnection() {

    private val updateItems: Callback<SearchForFacetValuesResponse?> = { response ->
        if (response != null) {
            viewModel.items.value = response.facetHits.map { hit ->
                FacetHits(hit.value, "", hit.count)
            }
        }
    }

    override fun connect() {
        super.connect()
        searcher.response.subscribePast(updateItems)
    }

    override fun disconnect() {
        super.disconnect()
        searcher.response.unsubscribe(updateItems)
    }
}
