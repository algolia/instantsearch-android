package com.algolia.instantsearch.filter.facet.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.AbstractConnection
import com.algolia.instantsearch.filter.facet.FacetListViewModel
import com.algolia.instantsearch.searcher.SearcherQuery
import com.algolia.instantsearch.searcher.addFacet
import com.algolia.search.model.Attribute
import com.algolia.search.model.response.ResponseSearch

internal data class FacetListConnectionSearcher(
    private val viewModel: FacetListViewModel,
    private val searcher: SearcherQuery<*, ResponseSearch>,
    private val attribute: Attribute,
) : AbstractConnection() {

    private val updateItems: Callback<ResponseSearch?> = { response ->
        if (response != null) {
            val disjunctiveFacets = response.disjunctiveFacetsOrNull?.get(attribute)

            viewModel.items.value = disjunctiveFacets ?: response.facetsOrNull.orEmpty()[attribute].orEmpty()
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
