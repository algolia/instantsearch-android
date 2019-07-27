package com.algolia.instantsearch.helper.filter.toggle

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.selectable.connectionView
import com.algolia.instantsearch.helper.filter.FilterPresenter
import com.algolia.instantsearch.helper.filter.FilterPresenterImpl
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState


public fun FilterToggleViewModel.connectionView(
    view: FilterToggleView,
    presenter: FilterPresenter = FilterPresenterImpl()
): Connection {
    return connectionView(view, presenter)
}

public fun FilterToggleViewModel.connectionFilterState(
    filterState: FilterState,
    groupID: FilterGroupID
): Connection {
    return FilterToggleConnectionFilterState(this, filterState, groupID)
}

public fun FilterToggleWidget.connectionView(
    view: FilterToggleView,
    presenter: FilterPresenter = FilterPresenterImpl()
): Connection {
    return viewModel.connectionView(view, presenter)
}