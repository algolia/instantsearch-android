package com.algolia.instantsearch.core.selectable.list


public fun <T> SelectableListViewModel<T, T>.connectView(
    view: SelectableListView<T>
) {
    fun setItem(items: List<T>, selections: Set<T>) {
        val item = items.map { it to selections.contains(it) }

        view.setItem(item)
    }

    setItem(item, selections)
    view.onClick = { selection -> computeSelections(selection) }
    onItemChanged += { items -> setItem(items, selections) }
    onSelectionsChanged += { selections -> setItem(item, selections) }
}