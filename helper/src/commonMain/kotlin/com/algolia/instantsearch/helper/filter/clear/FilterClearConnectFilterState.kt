package com.algolia.instantsearch.helper.filter.clear

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState


public fun FilterClearViewModel.connectionFilterState(
    filterState: FilterState,
    groupIDs: List<FilterGroupID>,
    mode: ClearMode
): Connection {
    return FilterClearConnectionFilterState(this, filterState, groupIDs, mode)
}