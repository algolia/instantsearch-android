package com.algolia.instantsearch.helper.filter.facet


public fun FacetListViewModel.connectView(
    view: FacetListView,
    presenter: FacetListPresenter? = null
) {

    fun setItems() {
        val item = getFacetListItems()

        view.setItems(presenter?.invoke(item) ?: item)
    }

    view.onSelection = { facet -> select(facet.value) }
    items.subscribePast { setItems() }
    selections.subscribe { setItems() }
}