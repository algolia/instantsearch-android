package com.algolia.instantsearch.core.selectable.segment

import com.algolia.instantsearch.core.Presenter
import com.algolia.instantsearch.core.connection.ConnectionImpl


public class SelectableSegmentConnectionView<K, I, O>(
    public val viewModel: SelectableSegmentViewModel<K, I>,
    public val view: SelectableSegmentView<K, O>,
    public val presenter: Presenter<I, O>
) : ConnectionImpl() {

    private fun Map<K, I>.present(): Map<K, O> {
        return map { it.key to presenter(it.value) }.toMap()
    }

    private val updateSegment: (Map<K, I>) -> Unit = { segment ->
        view.setSegment(segment.present())
    }

    override fun connect() {
        super.connect()
        viewModel.segment.subscribePast(updateSegment)
        viewModel.selected.subscribePast(view::setSelected)
        view.onSelectionChange = (viewModel.eventSelection::send)
    }

    override fun disconnect() {
        super.disconnect()
        viewModel.segment.unsubscribe(updateSegment)
        viewModel.selected.unsubscribe(view::setSelected)
        view.onSelectionChange = null
    }
}