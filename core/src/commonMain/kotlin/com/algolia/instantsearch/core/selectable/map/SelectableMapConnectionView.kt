package com.algolia.instantsearch.core.selectable.map

import com.algolia.instantsearch.core.Presenter
import com.algolia.instantsearch.core.connection.ConnectionImpl


internal class SelectableMapConnectionView<K, I, O>(
    public val viewModel: SelectableMapViewModel<K, I>,
    public val view: SelectableMapView<K, O>,
    public val presenter: Presenter<I, O>
) : ConnectionImpl() {

    private fun Map<K, I>.present(): Map<K, O> {
        return map { it.key to presenter(it.value) }.toMap()
    }

    private val updateSegment: (Map<K, I>) -> Unit = { segment ->
        view.setMap(segment.present())
    }

    override fun connect() {
        super.connect()
        viewModel.map.subscribePast(updateSegment)
        viewModel.selected.subscribePast(view::setSelected)
        view.onSelectionChange = (viewModel.eventSelection::send)
    }

    override fun disconnect() {
        super.disconnect()
        viewModel.map.unsubscribe(updateSegment)
        viewModel.selected.unsubscribe(view::setSelected)
        view.onSelectionChange = null
    }
}