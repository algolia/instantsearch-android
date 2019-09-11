package com.algolia.instantsearch.core.number.range

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.CallbackImpl
import com.algolia.instantsearch.core.CallbackRange
import com.algolia.instantsearch.core.connection.ConnectionImpl


internal data class NumberRangeConnectionView<T>(
    private val viewModel: NumberRangeViewModel<T>,
    private val view: NumberRangeView<T>
) : ConnectionImpl() where T : Number, T : Comparable<T> {

    private val updateBounds: Callback<Range<T>?> = CallbackRange { bounds ->
        view.setBounds(bounds)
    }
    private val updateRange: Callback<Range<T>?> = CallbackRange { range ->
        view.setRange(range)
    }

    override fun connect() {
        super.connect()
        viewModel.bounds.subscribePast(updateBounds)
        viewModel.range.subscribePast(updateRange)
        view.onRangeChanged = CallbackRange { viewModel.eventRange.send(it) }
    }

    override fun disconnect() {
        super.disconnect()
        viewModel.bounds.unsubscribe(updateBounds)
        viewModel.range.unsubscribe(updateRange)
        view.onRangeChanged = null
    }
}