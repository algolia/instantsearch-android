package com.algolia.instantsearch.helper.filter.map

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.selectable.map.SelectableMapView
import com.algolia.instantsearch.core.selectable.map.connectView
import com.algolia.instantsearch.helper.filter.FilterPresenter
import com.algolia.instantsearch.helper.filter.FilterPresenterImpl
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import kotlin.jvm.JvmOverloads

/**
 * Connects this FilterMapViewModel to a FilterState, updating its filter when the selection changes
 * and updating the viewModel's data when the filters change.
 */
@JvmOverloads
public fun FilterMapViewModel.connectFilterState(
    filterState: FilterState,
    groupID: FilterGroupID = FilterGroupID(FilterOperator.And)
): Connection {
    return FilterMapConnectionFilterState(this, filterState, groupID)
}

/**
 * Connects this FilterMapViewModel to a SelectableMapView,
 * updating it when the item or the selection changes.
 */
@JvmOverloads
public fun FilterMapViewModel.connectView(
    view: SelectableMapView<Int, String>,
    presenter: FilterPresenter = FilterPresenterImpl()
): Connection {
    return connectView(view, presenter)
}

/**
 * Connects this FilterMapConnector to a SelectableMapView,
 * updating it when the item or the selection changes.
 */
@JvmOverloads
public fun FilterMapConnector.connectView(
    view: SelectableMapView<Int, String>,
    presenter: FilterPresenter = FilterPresenterImpl()
): Connection {
    return viewModel.connectView(view, presenter)
}