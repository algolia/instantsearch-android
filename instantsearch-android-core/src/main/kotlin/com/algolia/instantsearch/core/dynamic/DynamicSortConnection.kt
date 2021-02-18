package com.algolia.instantsearch.core.dynamic

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.dynamic.internal.DynamicSortToggleConnectionView

/**
 * Create a connection between a view to the dynamic sort components.
 *
 * @param view the view that will render dynamic sort toggle
 */
public fun DynamicSortViewModel.connectView(view: DynamicSortView): Connection {
    return DynamicSortToggleConnectionView(this, view)
}
