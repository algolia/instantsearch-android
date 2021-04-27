package com.algolia.instantsearch.helper.filter.facet.dynamic.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.filter.facet.dynamic.DynamicFacetView
import com.algolia.instantsearch.helper.filter.facet.dynamic.DynamicFacetViewModel
import com.algolia.instantsearch.helper.filter.facet.dynamic.FacetSelections
import com.algolia.search.model.Attribute
import com.algolia.search.model.rule.AttributedFacets
import com.algolia.search.model.search.Facet

internal class DynamicFacetConnectionView(
    val viewModel: DynamicFacetViewModel,
    val view: DynamicFacetView,
) : ConnectionImpl() {

    private val facetOrderSubscription: Callback<List<AttributedFacets>> = { facetOrder ->
        view.commit(facetOrder)
    }

    private val facetSelectionsSubscription: Callback<FacetSelections> = { facetSelections ->
        view.commit(facetSelections)
    }

    private val didSelect: (Attribute, Facet) -> Unit = { attribute, facet ->
        viewModel.toggleSelection(attribute = attribute, facetValue = facet.value)
    }

    override fun connect() {
        super.connect()
        view.didSelect = didSelect
        facetOrderSubscribePast()
        facetSelectionsSubscribePast()
    }

    private fun facetOrderSubscribePast() {
        facetOrderSubscription.invoke(viewModel.facetOrder)
        viewModel.onFacetOrderUpdated.subscribe(facetOrderSubscription)
    }

    private fun facetSelectionsSubscribePast() {
        facetSelectionsSubscription.invoke(viewModel.selections)
        viewModel.onSelectionsUpdated.subscribe(facetSelectionsSubscription)
    }

    override fun disconnect() {
        super.disconnect()
        view.didSelect = null
        viewModel.onFacetOrderUpdated.unsubscribe(facetOrderSubscription)
        viewModel.onSelectionsUpdated.unsubscribe(facetSelectionsSubscription)
    }
}
