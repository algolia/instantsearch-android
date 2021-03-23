package com.algolia.instantsearch.helper.filter.facet.dynamic.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.filter.facet.dynamic.DynamicFacetViewModel
import com.algolia.instantsearch.helper.searcher.SearcherIndex
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.rule.FacetMerchandising

internal class DynamicFacetConnectionSearcherIndex(
    val viewModel: DynamicFacetViewModel,
    val searcher: SearcherIndex<*>,
) : ConnectionImpl() {

    private val responseSubscription: Callback<ResponseSearch?> = { response ->
        val facetMerchandising = response?.rules?.consequence?.renderingContent?.facetMerchandising
        viewModel.facetOrder = facetMerchandising ?: FacetMerchandising()
    }

    private val errorSubscription: Callback<Throwable?> = { _ ->
        viewModel.facetOrder = FacetMerchandising()
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
