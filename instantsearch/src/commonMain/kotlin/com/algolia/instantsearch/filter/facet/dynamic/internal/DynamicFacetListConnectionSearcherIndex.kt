package com.algolia.instantsearch.filter.facet.dynamic.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.AbstractConnection
import com.algolia.instantsearch.filter.facet.dynamic.AttributedFacets
import com.algolia.instantsearch.filter.facet.dynamic.DynamicFacetListViewModel
import com.algolia.instantsearch.searcher.SearcherForHits
import com.algolia.search.model.Attribute
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.rule.FacetOrdering
import com.algolia.search.model.search.Facet

/**
 * Connection between a dynamic facets business logic and a searcher.
 *
 * @param viewModel dynamic facets business logic
 * @param searcher searcher that handles your searches
 */
internal class DynamicFacetListConnectionSearcherIndex(
    val viewModel: DynamicFacetListViewModel,
    val searcher: SearcherForHits<*>,
) : AbstractConnection() {

    private val responseSubscription: Callback<ResponseSearch?> = { response ->
        val facetOrdering = response?.renderingContentOrNull?.facetOrdering
        val facets = response?.facetsOrNull
        viewModel.orderedFacets = buildOrder(facetOrdering, facets)
    }

    private fun buildOrder(ordering: FacetOrdering?, facets: Map<Attribute, List<Facet>>?): List<AttributedFacets> {
        return if (ordering != null && facets != null) facetsOrder(facets, ordering) else emptyList()
    }

    private val errorSubscription: Callback<Throwable?> = { _ ->
        viewModel.orderedFacets = emptyList()
    }

    override fun connect() {
        super.connect()
        searcher.response.subscribe(responseSubscription)
        searcher.error.subscribe(errorSubscription)
    }

    override fun disconnect() {
        super.disconnect()
        searcher.response.unsubscribe(responseSubscription)
        searcher.error.unsubscribe(errorSubscription)
    }
}
