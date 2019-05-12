package com.algolia.instantsearch.helper.filter.list

import com.algolia.search.model.filter.Filter


public fun <T : Filter> FilterListViewModel<T>.connectView(
    view: FilterListView<T>
) {

    fun setSelectableItems(filters: List<T>, selections: Set<T>) {
        val selectableItems = filters.map { it to selections.contains(it) }

        view.setSelectableItems(selectableItems)
    }

    setSelectableItems(items, selections)
    view.onClick = { filter -> computeSelections(filter) }
    onItemsChanged += { items -> setSelectableItems(items, selections) }
    onSelectionsChanged += { selections -> setSelectableItems(items, selections) }
}