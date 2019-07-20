package com.algolia.instantsearch.core.selectable

import com.algolia.instantsearch.core.Presenter
import com.algolia.instantsearch.core.connection.ConnectionImpl


internal class SelectableItemConnectionView<I, O>(
    private val viewModel: SelectableItemViewModel<I>,
    private val view: SelectableItemView<O>,
    presenter: Presenter<I, O>
) : ConnectionImpl() {

    private val updateItem: (I) -> Unit = { item ->
        view.setItem(presenter(item))
    }
    private val updateIsSelected: (Boolean) -> Unit = { isSelected ->
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