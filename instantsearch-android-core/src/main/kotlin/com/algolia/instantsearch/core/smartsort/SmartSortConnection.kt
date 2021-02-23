package com.algolia.instantsearch.core.smartsort

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.smartsort.internal.SmartSortConnectionView

/**
 * Create a connection between a view and the smart sort components.
 *
 * @param view the view that will render smart sort toggle
 * @param presenter defines the way we want to interact with a priority value
 */
public fun <T> SmartSortViewModel.connectView(view: SmartSortView<T>, presenter: SmartSortPresenter<T>): Connection {
    return SmartSortConnectionView(this, view, presenter)
}

/**
 * Create a connection between a view and the smart sort components.
 *
 * @param view the view that will render smart sort toggle
 */
public fun SmartSortViewModel.connectView(view: SmartSortPriorityView): Connection {
    return connectView(view) { it }
}
