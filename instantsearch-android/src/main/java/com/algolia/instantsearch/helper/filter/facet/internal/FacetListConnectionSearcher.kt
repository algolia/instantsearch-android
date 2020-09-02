package com.algolia.instantsearch.helper.filter.facet.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.helper.filter.facet.FacetListViewModel
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.addFacet
import com.algolia.search.model.Attribute
import com.algolia.search.model.response.ResponseSearch

internal data class FacetListConnectionSearcher(
    private val viewModel: FacetListViewModel,
    private val searcher: SearcherSingleIndex,
    private val attribute: Attribute,
) : ConnectionImpl() {

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
