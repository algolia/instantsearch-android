package com.algolia.instantsearch.helper.filter.toggle

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.autoConnect
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.filter.Filter


public fun FilterToggleViewModel.connectFilterState(
    filterState: FilterState,
    default: Filter? = null,
    groupID: FilterGroupID = FilterGroupID(item.value.attribute, FilterOperator.And),
    connect: Boolean = true
): Connection {
    return FilterToggleConnectionFilterState(this, filterState, default, groupID).autoConnect(connect)
}