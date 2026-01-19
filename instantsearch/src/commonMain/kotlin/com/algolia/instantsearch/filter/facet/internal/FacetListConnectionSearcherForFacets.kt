package com.algolia.instantsearch.filter.facet.internal

import com.algolia.client.model.search.SearchResponse
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.AbstractConnection
import com.algolia.instantsearch.filter.Facet
import com.algolia.instantsearch.filter.facet.FacetListViewModel
import com.algolia.instantsearch.searcher.SearcherQuery

internal data class FacetListConnectionSearcherForFacets(
    private val viewModel: FacetListViewModel,
    private val searcher: SearcherQuery<*, SearchResponse>,
) : AbstractConnection() {

    private val updateItems: Callback<SearchResponse?> = { response ->
        if (response != null) {
            viewModel.items.value = response.facets.orEmpty().values.flatMap { facets ->
                facets.map { (value, count) -> Facet(value, count) }
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
