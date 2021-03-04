package com.algolia.instantsearch.core.relevantsort

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.relevantsort.internal.RelevantSortConnectionView

/**
 * Create a connection between a view and the relevant sort components.
 *
 * @param view the view that will render relevant sort toggle
 * @param presenter defines the way we want to interact with a priority value
 */
public fun <T> RelevantSortViewModel.connectView(view: RelevantSortView<T>, presenter: RelevantSortPresenter<T>): Connection {
    return RelevantSortConnectionView(this, view, presenter)
}

/**
 * Create a connection between a view and the relevant sort components.
 *
 * @param view the view that will render relevant sort toggle
 */
public fun RelevantSortViewModel.connectView(view: RelevantSortPriorityView): Connection {
    return connectView(view) { it }
}
