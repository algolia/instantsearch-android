package com.algolia.instantsearch.core.selectable.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.Presenter
import com.algolia.instantsearch.core.connection.AbstractConnection
import com.algolia.instantsearch.core.selectable.SelectableItemView
import com.algolia.instantsearch.core.selectable.SelectableItemViewModel

internal data class SelectableItemConnectionView<I, O>(
    private val viewModel: SelectableItemViewModel<I>,
    private val view: SelectableItemView<O>,
    private val presenter: Presenter<I, O>
) : AbstractConnection() {

    private val updateItem: Callback<I> = { item ->
        view.setItem(presenter(item))
    }
    private val updateIsSelected: Callback<Boolean> = { isSelected ->
        view.setIsSelected(isSelected)
    }

    override fun connect() {
        super.connect()
        viewModel.item.subscribePast(updateItem)
        viewModel.isSelected.subscribePast(updateIsSelected)
        view.onSelectionChanged = (viewModel.eventSelection::send)
    }

    override fun disconnect() {
        super.disconnect()
        viewModel.item.unsubscribe(updateItem)
        viewModel.isSelected.unsubscribe(updateIsSelected)
        view.onSelectionChanged = null
    }
}
