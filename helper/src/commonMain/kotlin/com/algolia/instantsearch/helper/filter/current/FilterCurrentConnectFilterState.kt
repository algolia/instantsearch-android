package com.algolia.instantsearch.helper.filter.current

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.autoConnect
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState


public fun FilterCurrentViewModel.connectFilterState(
    filterState: FilterState,
    groupID: FilterGroupID? = null,
    connect: Boolean = true
): Connection {
    return FilterCurrentConnectionFilterState(this, filterState, groupID).autoConnect(connect)
}