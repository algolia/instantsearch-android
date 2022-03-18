package com.algolia.instantsearch.filter.map

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.selectable.map.SelectableMapView
import com.algolia.instantsearch.core.selectable.map.connectView
import com.algolia.instantsearch.filter.FilterPresenter
import com.algolia.instantsearch.filter.FilterPresenterImpl
import com.algolia.instantsearch.filter.map.internal.FilterMapConnectionFilterState
import com.algolia.instantsearch.filter.state.FilterGroupID
import com.algolia.instantsearch.filter.state.FilterOperator
import com.algolia.instantsearch.filter.state.FilterState

public fun FilterMapViewModel.connectFilterState(
    filterState: FilterState,
    groupID: FilterGroupID = FilterGroupID(FilterOperator.And),
): Connection {
    return FilterMapConnectionFilterState(this, filterState, groupID)
}

public fun FilterMapViewModel.connectView(
    view: SelectableMapView<Int, String>,
    presenter: FilterPresenter = FilterPresenterImpl(),
): Connection {
    return connectView(view, presenter)
}

/**
 * Create a connection between a view and the filter map components.
 *
 * @param view the view that renders the filter map
 * @param presenter defines the way we want to display filters
 */
public fun FilterMapConnector.connectView(
    view: SelectableMapView<Int, String>,
    presenter: FilterPresenter = FilterPresenterImpl(),
): Connection {
    return viewModel.connectView(view, presenter)
}
