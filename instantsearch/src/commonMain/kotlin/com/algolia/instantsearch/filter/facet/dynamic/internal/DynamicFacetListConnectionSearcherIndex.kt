package com.algolia.instantsearch.filter.facet.dynamic.internal

import com.algolia.client.model.search.FacetOrdering
import com.algolia.client.model.search.SearchResponse
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.AbstractConnection
import com.algolia.instantsearch.filter.facet.dynamic.AttributedFacets
import com.algolia.instantsearch.filter.facet.dynamic.DynamicFacetListViewModel
import com.algolia.instantsearch.filter.Facet
import com.algolia.instantsearch.searcher.SearcherForHits

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

    private val responseSubscription: Callback<SearchResponse?> = { response ->
        val facetOrdering = response?.renderingContent?.facetOrdering
        val facetsRaw = response?.facets ?: emptyMap()
        // Convert v3 facets format (Map<String, Map<String, Int>>) to our format (Map<String, List<Facet>>)
        val facets = facetsRaw.mapValues { (_, counts) ->
            counts.map { (value, count) -> Facet(value, count) }
        }
        // In v3, there's no separate disjunctiveFacets - all facets are in the facets map
        viewModel.orderedFacets = buildOrder(facetOrdering, facets)
    }

    private fun buildOrder(ordering: FacetOrdering?, facets: Map<String, List<Facet>>?): List<AttributedFacets> {
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
