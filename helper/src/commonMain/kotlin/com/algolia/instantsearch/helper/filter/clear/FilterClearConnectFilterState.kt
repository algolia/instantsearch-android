package com.algolia.instantsearch.helper.filter.clear

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.autoConnect
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState


public fun FilterClearViewModel.connectFilterState(
    filterState: FilterState,
    groupIDs: List<FilterGroupID> = listOf(),
    mode: ClearMode = ClearMode.Specified,
    connect: Boolean = true
): Connection {
    return FilterClearConnectionFilterState(this, filterState, groupIDs, mode).autoConnect(connect)
}