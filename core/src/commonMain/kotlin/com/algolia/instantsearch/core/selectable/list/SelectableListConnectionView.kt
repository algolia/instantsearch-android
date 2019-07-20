package com.algolia.instantsearch.core.selectable.list

import com.algolia.instantsearch.core.connection.ConnectionImpl


internal class SelectableListConnectionView<T>(
    private val viewModel: SelectableListViewModel<T, T>,
    private val view: SelectableListView<T>
) : ConnectionImpl() {

    private val updateItems: (List<T>) -> Unit = { items ->
        val item = items.map { it to viewModel.selections.value.contains(it) }

        view.setItems(item)
    }

    private val updateSelections: (Set<T>) -> Unit = { selections ->
        val item = viewModel.items.value.map { it to selections.contains(it) }

        view.setItems(item)
    }

    override fun connect() {
        super.connect()
        viewModel.items.subscribePast(updateItems)
        viewModel.selections.subscribe(updateSelections)
        view.onSelection = { selection -> viewModel.select(selection) }
    }

    override fun disconnect() {
        super.disconnect()
        viewModel.items.unsubscribe(updateItems)
        viewModel.selections.unsubscribe(updateSelections)
        view.onSelection = null
    }
}