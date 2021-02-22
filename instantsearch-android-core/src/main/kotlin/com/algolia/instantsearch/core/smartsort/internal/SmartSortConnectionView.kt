package com.algolia.instantsearch.core.smartsort.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.smartsort.SmartSortPriority
import com.algolia.instantsearch.core.smartsort.SmartSortView
import com.algolia.instantsearch.core.smartsort.SmartSortViewModel

/**
 * Connection between smart sort's view model and view.
 */
internal class SmartSortConnectionView(
    private val viewModel: SmartSortViewModel,
    private val view: SmartSortView
) : ConnectionImpl() {

    val callback: Callback<SmartSortPriority?> = {
        view.setPriority(it)
    }

    override fun connect() {
        super.connect()
        view.didToggle = { viewModel.toggle() }
        viewModel.priority.subscribePast(callback)
    }

    override fun disconnect() {
        super.disconnect()
        view.didToggle = null
        viewModel.priority.unsubscribe(callback)
    }
}
