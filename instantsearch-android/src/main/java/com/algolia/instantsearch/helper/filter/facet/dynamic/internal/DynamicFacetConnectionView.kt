package com.algolia.instantsearch.helper.filter.facet.dynamic.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.filter.facet.dynamic.DynamicFacetView
import com.algolia.instantsearch.helper.filter.facet.dynamic.DynamicFacetViewModel
import com.algolia.instantsearch.helper.filter.facet.dynamic.FacetSelections
import com.algolia.search.model.rule.FacetMerchandising

internal class DynamicFacetConnectionView(
    val viewModel: DynamicFacetViewModel,
    val view: DynamicFacetView,
) : ConnectionImpl() {

    private val facetOrderSubscription: Callback<FacetMerchandising> = { facetMerchandising ->
        view.commit(facetMerchandising.facetOrder)
    }

    private val facetSelectionsSubscription: Callback<FacetSelections> = { facetSelections ->
        view.commit(facetSelections)
    }

    override fun connect() {
        super.connect()
        view.didSelect = { attribute, facet ->
            viewModel.toggleSelection(attribute = attribute, facetValue = facet.value)
        }
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
