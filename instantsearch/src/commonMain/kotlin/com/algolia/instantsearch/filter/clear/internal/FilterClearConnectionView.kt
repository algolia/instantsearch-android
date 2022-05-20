package com.algolia.instantsearch.filter.clear.internal

import com.algolia.instantsearch.core.connection.AbstractConnection
import com.algolia.instantsearch.filter.clear.FilterClearView
import com.algolia.instantsearch.filter.clear.FilterClearViewModel

internal data class FilterClearConnectionView(
    private val viewModel: FilterClearViewModel,
    private val view: FilterClearView,
) : AbstractConnection() {

    override fun connect() {
        super.connect()
        view.onClear = (viewModel.eventClear::send)
    }

    override fun disconnect() {
        super.disconnect()
        view.onClear = null
    }
}
