package com.algolia.instantsearch.filter.facet.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.AbstractConnection
import com.algolia.instantsearch.filter.facet.FacetListViewModel
import com.algolia.instantsearch.searcher.SearcherQuery

internal data class FacetListConnectionSearcherForFacets(
    private val viewModel: FacetListViewModel,
    private val searcher: SearcherQuery<*, ResponseSearchForFacets>,
) : AbstractConnection() {

    private val updateItems: Callback<ResponseSearchForFacets?> = { response ->
        if (response != null) {
            viewModel.items.value = response.facets
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
