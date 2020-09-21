package com.algolia.instantsearch.helper.filter.clear

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState

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

public fun FilterClearConnector.connectView(
    view: FilterClearView,
): Connection {
    return viewModel.connectView(view)
}
