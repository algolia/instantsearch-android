@file:JvmName("FilterToggle")

package com.algolia.instantsearch.helper.filter.toggle

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.selectable.connectView
import com.algolia.instantsearch.helper.filter.FilterPresenter
import com.algolia.instantsearch.helper.filter.FilterPresenterImpl
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads

/**
 * Connects this FilterToggleViewModel to a FilterToggleView,
 * updating it when the filter is toggled.
 */
public fun FilterToggleViewModel.connectView(
    view: FilterToggleView,
    presenter: FilterPresenter = FilterPresenterImpl()
): Connection {
    return connectView(view, presenter)
}

/**
 * Connects this FilterToggleViewModel to a FilterState, updating its filters when toggled
 * and toggling the filter off when it is removed from the filterState.
 */
@JvmOverloads
public fun FilterToggleViewModel.connectFilterState(
    filterState: FilterState,
    groupID: FilterGroupID = FilterGroupID(item.value.attribute, FilterOperator.And)
): Connection {
    return FilterToggleConnectionFilterState(this, filterState, groupID)
}

/**
 * Connects this FilterToggleConnector to a FilterToggleView,
 * updating it when the filter is toggled.
 */
@JvmOverloads
public fun FilterToggleConnector.connectView(
    view: FilterToggleView,
    presenter: FilterPresenter = FilterPresenterImpl()
): Connection {
    return viewModel.connectView(view, presenter)
}