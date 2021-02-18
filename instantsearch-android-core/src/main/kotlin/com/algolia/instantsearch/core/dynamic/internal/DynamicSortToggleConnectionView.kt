package com.algolia.instantsearch.core.dynamic.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.dynamic.DynamicSortPriority
import com.algolia.instantsearch.core.dynamic.DynamicSortView
import com.algolia.instantsearch.core.dynamic.DynamicSortViewModel

/**
 * Connection between dynamic sort's view model and view.
 */
internal class DynamicSortToggleConnectionView(
    private val viewModel: DynamicSortViewModel,
    private val view: DynamicSortView
) : ConnectionImpl() {

    val callback: Callback<DynamicSortPriority?> = {
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
