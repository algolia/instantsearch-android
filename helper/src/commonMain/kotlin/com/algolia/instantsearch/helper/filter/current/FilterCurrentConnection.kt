package com.algolia.instantsearch.helper.filter.current

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState


public fun FilterCurrentViewModel.connectionView(
    view: FilterCurrentView,
    presenter: FilterCurrentPresenter
): Connection {
    return FilterCurrentConnectionView(this, view, presenter)
}

public fun FilterCurrentWidget.connectionView(
    view: FilterCurrentView,
    presenter: FilterCurrentPresenter = FilterCurrentPresenterImpl()
): Connection {
    return viewModel.connectionView(view, presenter)
}

public fun FilterCurrentViewModel.connectionFilterState(
    filterState: FilterState,
    groupID: List<FilterGroupID> = listOf()
): Connection {
    return FilterCurrentConnectionFilterState(this, filterState, groupID)
}