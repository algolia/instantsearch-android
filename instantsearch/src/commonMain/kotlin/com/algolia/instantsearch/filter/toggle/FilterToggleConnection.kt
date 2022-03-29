package com.algolia.instantsearch.filter.toggle

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.selectable.connectView
import com.algolia.instantsearch.filter.FilterPresenter
import com.algolia.instantsearch.filter.FilterPresenterImpl
import com.algolia.instantsearch.filter.state.FilterGroupID
import com.algolia.instantsearch.filter.state.FilterOperator
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.filter.toggle.internal.FilterToggleConnectionFilterState

public fun FilterToggleViewModel.connectView(
    view: FilterToggleView,
    presenter: FilterPresenter = FilterPresenterImpl(),
): Connection {
    return connectView(view, presenter)
}

public fun FilterToggleViewModel.connectFilterState(
    filterState: FilterState,
    groupID: FilterGroupID = FilterGroupID(item.value.attribute, FilterOperator.And),
): Connection {
    return FilterToggleConnectionFilterState(this, filterState, groupID)
}

/**
 * Create a connection between a view and the filter toggle components
 *
 * @param view the view that will render the filter toggle
 * @param presenter a presenter describing how to display a filter
 */
public fun FilterToggleConnector.connectView(
    view: FilterToggleView,
    presenter: FilterPresenter = FilterPresenterImpl(),
): Connection {
    return viewModel.connectView(view, presenter)
}
