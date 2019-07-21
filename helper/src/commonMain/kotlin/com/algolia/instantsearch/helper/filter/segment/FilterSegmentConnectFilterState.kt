package com.algolia.instantsearch.helper.filter.segment

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.autoConnect
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState


public fun FilterSegmentViewModel.connectFilterState(
    filterState: FilterState,
    groupID: FilterGroupID = FilterGroupID(FilterOperator.And),
    connect: Boolean = true
): Connection {
    return FilterSegmentConnectionFilterState(this, filterState, groupID).autoConnect(connect)
}