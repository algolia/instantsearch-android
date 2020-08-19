package com.algolia.instantsearch.helper.filter.current

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState


public fun FilterCurrentViewModel.connectView(
    view: FilterCurrentView,
    presenter: FilterCurrentPresenter = FilterCurrentPresenterImpl()
): Connection {
    return FilterCurrentConnectionView(this, view, presenter)
}

public fun FilterCurrentConnector.connectView(
    view: FilterCurrentView,
    presenter: FilterCurrentPresenter = FilterCurrentPresenterImpl()
): Connection {
    return viewModel.connectView(view, presenter)
}

public fun FilterCurrentViewModel.connectFilterState(
    filterState: FilterState,
    groupID: List<FilterGroupID> = listOf()
): Connection {
    return FilterCurrentConnectionFilterState(this, filterState, groupID)
}