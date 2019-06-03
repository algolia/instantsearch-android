package com.algolia.instantsearch.helper.filter.list

import com.algolia.search.model.filter.Filter


public fun <T : Filter> FilterListViewModel<T>.connectView(
    view: FilterListView<T>
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