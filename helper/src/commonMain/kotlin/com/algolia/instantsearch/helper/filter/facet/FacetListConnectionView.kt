package com.algolia.instantsearch.helper.filter.facet

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.Callback


internal data class FacetListConnectionView(
    private val viewModel: FacetListViewModel,
    private val view: FacetListView,
    private val presenter: FacetListPresenter?
) : ConnectionImpl() {

    private val updateFacets: Callback<List<FacetListItem>> = { facets ->
        view.setItems(presenter?.invoke(facets) ?: facets)
    }

    override fun connect() {
        super.connect()
        viewModel.facets.subscribePast(updateFacets)
        view.onSelection = { facet -> viewModel.select(facet.value) }
    }

    override fun disconnect() {
        super.disconnect()
        viewModel.facets.unsubscribe(updateFacets)
        view.onSelection = null
    }
}