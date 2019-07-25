package com.algolia.instantsearch.helper.filter.current

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState


public fun FilterCurrentViewModel.connectionFilterState(
    filterState: FilterState,
    groupID: List<FilterGroupID> = listOf()
): Connection {
    return FilterCurrentConnectionFilterState(this, filterState, groupID)
}