package com.algolia.instantsearch.core.selectable.list.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.AbstractConnection
import com.algolia.instantsearch.core.selectable.list.SelectableListView
import com.algolia.instantsearch.core.selectable.list.SelectableListViewModel

internal data class SelectableListConnectionView<T>(
    private val viewModel: SelectableListViewModel<T, T>,
    private val view: SelectableListView<T>
) : AbstractConnection() {

    private val updateItems: Callback<List<T>> = { items ->
        val item = items.map { it to viewModel.selections.value.contains(it) }

        view.setItems(item)
    }
    private val updateSelections: Callback<Set<T>> = { selections ->
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
