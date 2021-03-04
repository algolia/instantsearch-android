package com.algolia.instantsearch.core.relevantsort.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.relevantsort.RelevantSortPresenter
import com.algolia.instantsearch.core.relevantsort.RelevantSortPriority
import com.algolia.instantsearch.core.relevantsort.RelevantSortView
import com.algolia.instantsearch.core.relevantsort.RelevantSortViewModel

/**
 * Connection between relevant sort's view model and view.
 *
 * @param viewModel the component handling relevant sort logic
 * @param view the view that will render relevant sort toggle
 * @param presenter defines the way we want to interact with a priority value
 */
internal class RelevantSortConnectionView<T>(
    private val viewModel: RelevantSortViewModel,
    private val view: RelevantSortView<T>,
    private val presenter: RelevantSortPresenter<T>,
) : ConnectionImpl() {

    val callback: Callback<RelevantSortPriority?> = { priority ->
        view.updateView(presenter(priority))
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
