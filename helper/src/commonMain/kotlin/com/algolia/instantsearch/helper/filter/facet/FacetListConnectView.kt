package com.algolia.instantsearch.helper.filter.facet


public fun FacetListViewModel.connectView(
    view: FacetListView,
    presenter: FacetListPresenter? = null
) {
    fun setItem() {
        val item = getFacetListItems()

        view.setItem(presenter?.invoke(item) ?: item)
    }

    setItem()
    view.onClick = { facet -> computeSelections(facet.value) }
    onItemChanged += { setItem() }
    onSelectionsChanged += { setItem() }
}