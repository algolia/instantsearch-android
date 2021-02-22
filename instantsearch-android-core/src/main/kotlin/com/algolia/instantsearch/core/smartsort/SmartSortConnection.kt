package com.algolia.instantsearch.core.smartsort

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.smartsort.internal.SmartSortConnectionView

/**
 * Create a connection between a view to the smart sort components.
 *
 * @param view the view that will render smart sort toggle
 */
public fun SmartSortViewModel.connectView(view: SmartSortView): Connection {
    return SmartSortConnectionView(this, view)
}
