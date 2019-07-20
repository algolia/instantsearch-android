package com.algolia.instantsearch.helper.filter.list

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.autoConnect
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.Filters
import com.algolia.search.model.filter.Filter


public fun FilterListViewModel.Facet.connectFilterState(
    filterState: FilterState,
    groupID: FilterGroupID = FilterGroupID(FilterOperator.Or),
    connect: Boolean = true
): Connection {
    return connect(filterState, groupID, connect) { getFacetFilters(groupID) }
}

public fun FilterListViewModel.Numeric.connectFilterState(
    filterState: FilterState,
    groupID: FilterGroupID = FilterGroupID(FilterOperator.And),
    connect: Boolean = true
): Connection {
    return connect(filterState, groupID, connect) { getNumericFilters(groupID) }
}


public fun FilterListViewModel.Tag.connectFilterState(
    filterState: FilterState,
    groupID: FilterGroupID = FilterGroupID(FilterOperator.And),
    connect: Boolean = true
): Connection {
    return connect(filterState, groupID, connect) { getTagFilters(groupID) }
}

public fun FilterListViewModel.All.connectFilterState(
    filterState: FilterState,
    groupID: FilterGroupID = FilterGroupID(FilterOperator.And),
    connect: Boolean = true
): Connection {
    return connect(filterState, groupID, connect) { getFilters(groupID) }
}

internal inline fun <reified T : Filter> FilterListViewModel<T>.connect(
    filterState: FilterState,
    groupID: FilterGroupID,
    connect: Boolean,
    noinline getSelections: Filters.() -> Set<T>
): Connection {
    return FilterListConnectionFilterState(this, filterState, groupID, getSelections).autoConnect(connect)
}