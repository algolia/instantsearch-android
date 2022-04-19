package com.algolia.instantsearch.filter.current

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.filter.current.internal.FilterCurrentConnectionFilterState
import com.algolia.instantsearch.filter.current.internal.FilterCurrentConnectionView
import com.algolia.instantsearch.filter.state.FilterGroupID
import com.algolia.instantsearch.filter.state.FilterState

public fun FilterCurrentViewModel.connectView(
    view: FilterCurrentView,
    presenter: FilterCurrentPresenter = DefaultFilterCurrentPresenter(),
): Connection {
    return FilterCurrentConnectionView(this, view, presenter)
}

/**
 * Create a connection between a view and the current filters components.
 *
 * @param view the view that will render the current filters
 * @param presenter defines the way we want to display a filter
 */
public fun FilterCurrentConnector.connectView(
    view: FilterCurrentView,
    presenter: FilterCurrentPresenter = DefaultFilterCurrentPresenter(),
): Connection {
    return viewModel.connectView(view, presenter)
}

public fun FilterCurrentViewModel.connectFilterState(
    filterState: FilterState,
    groupID: List<FilterGroupID> = listOf(),
): Connection {
    return FilterCurrentConnectionFilterState(this, filterState, groupID)
}
