package com.algolia.instantsearch.core.smartsort.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.smartsort.SmartSortPresenter
import com.algolia.instantsearch.core.smartsort.SmartSortPriority
import com.algolia.instantsearch.core.smartsort.SmartSortView
import com.algolia.instantsearch.core.smartsort.SmartSortViewModel

/**
 * Connection between smart sort's view model and view.
 *
 * @param viewModel the component handling Smart sort logic
 * @param view the view that will render smart sort toggle
 * @param presenter defines the way we want to interact with a priority value
 */
internal class SmartSortConnectionView(
    private val viewModel: SmartSortViewModel,
    private val view: SmartSortView,
    private val presenter: SmartSortPresenter?,
) : ConnectionImpl() {

    val callback: Callback<SmartSortPriority?> = { priority ->
        view.setPriority(presenter?.invoke(priority) ?: priority)
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
