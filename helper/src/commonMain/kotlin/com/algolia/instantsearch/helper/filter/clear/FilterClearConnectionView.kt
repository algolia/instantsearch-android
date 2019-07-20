package com.algolia.instantsearch.helper.filter.clear

import com.algolia.instantsearch.core.connection.ConnectionImpl


internal class FilterClearConnectionView(
    private val viewModel: FilterClearViewModel,
    private val view: FilterClearView
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