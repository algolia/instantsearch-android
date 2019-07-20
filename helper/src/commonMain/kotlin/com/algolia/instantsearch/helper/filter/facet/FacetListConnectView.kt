package com.algolia.instantsearch.helper.filter.facet

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.autoConnect


public fun FacetListViewModel.connectView(
    view: FacetListView,
    presenter: FacetListPresenter? = null,
    connect: Boolean = true
): Connection {
    return FacetListConnectionView(this, view, presenter).autoConnect(connect)
}