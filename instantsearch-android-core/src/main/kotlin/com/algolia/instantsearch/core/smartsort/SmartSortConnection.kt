package com.algolia.instantsearch.core.smartsort

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.smartsort.internal.SmartSortConnectionView

/**
 * Create a connection between a view to the smart sort components.
 *
 * @param view the view that will render smart sort toggle
 * @param presenter defines the way we want to interact with a priority value
 */
public fun SmartSortViewModel.connectView(view: SmartSortView, presenter: SmartSortPresenter? = null): Connection {
    return SmartSortConnectionView(this, view, presenter)
}
