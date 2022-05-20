package com.algolia.instantsearch.filter.facet.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.AbstractConnection
import com.algolia.instantsearch.filter.facet.FacetListItem
import com.algolia.instantsearch.filter.facet.FacetListPresenter
import com.algolia.instantsearch.filter.facet.FacetListView
import com.algolia.instantsearch.filter.facet.FacetListViewModel

internal data class FacetListConnectionView(
    private val viewModel: FacetListViewModel,
    private val view: FacetListView,
    private val presenter: FacetListPresenter?,
) : AbstractConnection() {

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
