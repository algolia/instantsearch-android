package com.algolia.instantsearch.filter.clear

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.filter.clear.internal.FilterClearConnectionFilterState
import com.algolia.instantsearch.filter.clear.internal.FilterClearConnectionView
import com.algolia.instantsearch.filter.state.FilterGroupID
import com.algolia.instantsearch.filter.state.FilterState

public fun FilterClearViewModel.connectFilterState(
    filterState: FilterState,
    groupIDs: List<FilterGroupID> = listOf(),
    mode: ClearMode = ClearMode.Specified,
): Connection {
    return FilterClearConnectionFilterState(this, filterState, groupIDs, mode)
}

public fun FilterClearViewModel.connectView(
    view: FilterClearView,
): Connection {
    return FilterClearConnectionView(this, view)
}

/**
 * Create a connection between a view to the filter clear components.
 *
 * @param view the view that will render the clear filter UI
 */
public fun FilterClearConnector.connectView(
    view: FilterClearView,
): Connection {
    return viewModel.connectView(view)
}
