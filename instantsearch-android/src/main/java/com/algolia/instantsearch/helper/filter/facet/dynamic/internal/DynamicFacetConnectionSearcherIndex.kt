package com.algolia.instantsearch.helper.filter.facet.dynamic.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.filter.facet.dynamic.DynamicFacetViewModel
import com.algolia.instantsearch.helper.searcher.SearcherIndex
import com.algolia.search.model.Attribute
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.rule.AttributedFacets
import com.algolia.search.model.rule.FacetOrdering
import com.algolia.search.model.search.Facet

internal class DynamicFacetConnectionSearcherIndex(
    val viewModel: DynamicFacetViewModel,
    val searcher: SearcherIndex<*>,
) : ConnectionImpl() {

    private val responseSubscription: Callback<ResponseSearch?> = { response ->
        val facetOrdering = response?.rules?.consequence?.renderingContent?.facetMerchandising?.facetOrdering
        val facets = response?.facetsOrNull
        val facetOrder = if (facetOrdering != null && facets != null) buildOrder(facetOrdering, facets) else emptyList()
        viewModel.facetOrder = facetOrder
    }

    private fun buildOrder(
        facetOrdering: FacetOrdering?,
        facets: Map<Attribute, List<Facet>>?
    ): List<AttributedFacets> {
        if (facetOrdering != null && facets != null) {
            return BuildOrder(facetOrdering, facets).invoke()
        }
        return emptyList()
    }

    private val errorSubscription: Callback<Throwable?> = { _ ->
        viewModel.facetOrder = emptyList()
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
