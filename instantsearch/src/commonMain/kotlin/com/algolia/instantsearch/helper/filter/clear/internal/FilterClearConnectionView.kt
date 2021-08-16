package com.algolia.instantsearch.helper.filter.clear.internal

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.filter.clear.FilterClearView
import com.algolia.instantsearch.helper.filter.clear.FilterClearViewModel

internal data class FilterClearConnectionView(
    private val viewModel: FilterClearViewModel,
    private val view: FilterClearView,
) : ConnectionImpl() {

    override fun connect() {
        super.connect()
        view.onClear = (viewModel.eventClear::send)
    }

    override fun disconnect() {
        super.disconnect()
        view.onClear = null
    }
}
