package com.algolia.instantsearch.helper.filter.facet

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.helper.searcher.SearcherForFacets
import com.algolia.search.model.response.ResponseSearchForFacets


internal data class FacetListConnectionSearcherForFacets(
    private val viewModel: FacetListViewModel,
    private val searcher: SearcherForFacets
) : ConnectionImpl() {

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