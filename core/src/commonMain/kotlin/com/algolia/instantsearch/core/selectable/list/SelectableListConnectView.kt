package com.algolia.instantsearch.core.selectable.list


public fun <T> SelectableListViewModel<T, T>.connectView(
    view: SelectableListView<T>
) {
    fun setItem(filters: List<T>, selections: Set<T>) {
        val item = filters.map { it to selections.contains(it) }

        view.setItem(item)
    }

    setItem(item, selections)
    view.onClick = { filter -> computeSelections(filter) }
    onItemChanged += { items -> setItem(items, selections) }
    onSelectionsChanged += { selections -> setItem(item, selections) }
}