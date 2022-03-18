package com.algolia.instantsearch.filter.facet.dynamic.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.filter.facet.dynamic.AttributedFacets
import com.algolia.instantsearch.filter.facet.dynamic.DynamicFacetListView
import com.algolia.instantsearch.filter.facet.dynamic.DynamicFacetListViewModel
import com.algolia.instantsearch.filter.facet.dynamic.SelectionsPerAttribute
import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Facet

/**
 * Connection between a dynamic facets business logic and a controller.
 *
 * @param viewModel dynamic facets business logic
 * @param view view of the ordered list of facets and handling user interaction
 */
internal class DynamicFacetListConnectionView(
    val viewModel: DynamicFacetListViewModel,
    val view: DynamicFacetListView,
) : ConnectionImpl() {

    private val didSelect: (Attribute, Facet) -> Unit = { attribute, facet ->
        viewModel.toggleSelection(attribute = attribute, facetValue = facet.value)
    }

    private val selectionsChangedSubscription: Callback<SelectionsPerAttribute> = { selections ->
        view.setSelections(selections)
    }

    private val facetOrderSubscription: Callback<List<AttributedFacets>> = { orderedFacets ->
        view.setOrderedFacets(orderedFacets)
    }

    override fun connect() {
        super.connect()
        view.didSelect = didSelect
        facetSelectionsSubscribePast()
        facetOrderSubscribePast()
    }

    private fun facetSelectionsSubscribePast() {
        selectionsChangedSubscription.invoke(viewModel.selections)
        viewModel.onSelectionsChanged.subscribe(selectionsChangedSubscription)
    }

    private fun facetOrderSubscribePast() {
        facetOrderSubscription.invoke(viewModel.orderedFacets)
        viewModel.onFacetOrderChanged.subscribe(facetOrderSubscription)
    }

    override fun disconnect() {
        super.disconnect()
        view.didSelect = null
        viewModel.onSelectionsChanged.unsubscribe(selectionsChangedSubscription)
        viewModel.onFacetOrderChanged.unsubscribe(facetOrderSubscription)
    }
}
