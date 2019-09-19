@file:JvmName("FilterClear")

package com.algolia.instantsearch.helper.filter.clear

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads

/**
 * Connects this FilterClearViewModel to a FilterState, updating it when filters are cleared.
 */
@JvmOverloads
public fun FilterClearViewModel.connectFilterState(
    filterState: FilterState,
    groupIDs: List<FilterGroupID> = listOf(),
    mode: ClearMode = ClearMode.Specified
): Connection {
    return FilterClearConnectionFilterState(this, filterState, groupIDs, mode)
}

/**
 * Connects this FilterClearViewModel to a FilterClearView, updating it when filters are cleared.
 */
public fun FilterClearViewModel.connectView(
    view: FilterClearView
): Connection {
    return FilterClearConnectionView(this, view)
}

/**
 * Connects this FilterClearConnector to a FilterClearView, updating it when filters are cleared.
 */
public fun FilterClearConnector.connectView(
    view: FilterClearView
): Connection {
    return viewModel.connectView(view)
}