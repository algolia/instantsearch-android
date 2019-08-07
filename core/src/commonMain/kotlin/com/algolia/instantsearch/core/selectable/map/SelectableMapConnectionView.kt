package com.algolia.instantsearch.core.selectable.map

import com.algolia.instantsearch.core.Presenter
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.Callback


internal data class SelectableMapConnectionView<K, I, O>(
    private val viewModel: SelectableMapViewModel<K, I>,
    private val view: SelectableMapView<K, O>,
    private val presenter: Presenter<I, O>
) : ConnectionImpl() {

    private fun Map<K, I>.present(): Map<K, O> {
        return map { it.key to presenter(it.value) }.toMap()
    }

    private val updateMap: Callback<Map<K, I>> = { segment ->
        view.setMap(segment.present())
    }

    override fun connect() {
        super.connect()
        viewModel.map.subscribePast(updateMap)
        viewModel.selected.subscribePast(view::setSelected)
        view.onSelectionChange = (viewModel.eventSelection::send)
    }

    override fun disconnect() {
        super.disconnect()
        viewModel.map.unsubscribe(updateMap)
        viewModel.selected.unsubscribe(view::setSelected)
        view.onSelectionChange = null
    }
}