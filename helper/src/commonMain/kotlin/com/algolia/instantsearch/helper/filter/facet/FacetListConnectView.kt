package com.algolia.instantsearch.helper.filter.facet


public fun FacetListViewModel.connectView(
    view: FacetListView,
    presenter: ((List<FacetListItem>) -> List<FacetListItem>)? = null
) {

    fun setSelectableItems() {
        val selectableItems = getFacetListItems()

        view.setSelectableItems(presenter?.invoke(selectableItems) ?: selectableItems)
    }

    setSelectableItems()
    view.onClick = { facet -> computeSelections(facet.value) }
    onItemsChanged += { setSelectableItems() }
    onSelectionsChanged += { setSelectableItems() }
}