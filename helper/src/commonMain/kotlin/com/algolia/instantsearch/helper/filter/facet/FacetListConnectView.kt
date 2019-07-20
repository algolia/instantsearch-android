package com.algolia.instantsearch.helper.filter.facet


public fun FacetListViewModel.connectView(
    view: FacetListView,
    presenter: FacetListPresenter? = null
) {
    facets.subscribePast { view.setItems(presenter?.invoke(it) ?: it) }
    view.onSelection = { facet -> select(facet.value) }
}