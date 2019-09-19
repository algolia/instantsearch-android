@file:JvmName("FilterCurrent")

package com.algolia.instantsearch.helper.filter.current

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads

/**
 * Connects this FilterCurrentViewModel to a FilterCurrentView,
 * updating it when the filters change and removing a filter when the user selects it.
 */
@JvmOverloads
public fun FilterCurrentViewModel.connectView(
    view: FilterCurrentView,
    presenter: FilterCurrentPresenter = FilterCurrentPresenterImpl()
): Connection {
    return FilterCurrentConnectionView(this, view, presenter)
}

@JvmOverloads
public fun FilterCurrentConnector.connectView(
    view: FilterCurrentView,
    presenter: FilterCurrentPresenter = FilterCurrentPresenterImpl()
): Connection {
    return viewModel.connectView(view, presenter)
}

@JvmOverloads
public fun FilterCurrentViewModel.connectFilterState(
    filterState: FilterState,
    groupID: List<FilterGroupID> = listOf()
): Connection {
    return FilterCurrentConnectionFilterState(this, filterState, groupID)
}