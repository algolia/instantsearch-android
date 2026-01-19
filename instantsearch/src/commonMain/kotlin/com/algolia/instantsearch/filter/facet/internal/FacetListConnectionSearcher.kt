package com.algolia.instantsearch.filter.facet.internal

import com.algolia.client.model.search.SearchResponse
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.AbstractConnection
import com.algolia.instantsearch.filter.facet.FacetListViewModel

import com.algolia.instantsearch.searcher.SearcherQuery
import com.algolia.instantsearch.searcher.addFacet

internal data class FacetListConnectionSearcher(
    private val viewModel: FacetListViewModel,
    private val searcher: SearcherQuery<*, SearchResponse>,
    private val attribute: String,
) : AbstractConnection() {

    private val updateItems: Callback<SearchResponse?> = { response ->
        if (response != null) {
            val disjunctiveFacets = response.disjunctiveFacetsOrNull?.get(attribute)

            viewModel.items.value = disjunctiveFacets ?: response.facets.orEmpty()[attribute].orEmpty()
        }
    }

    init {
        searcher.query.addFacet(attribute)
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
