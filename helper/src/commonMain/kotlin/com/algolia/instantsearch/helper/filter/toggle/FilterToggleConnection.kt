package com.algolia.instantsearch.helper.filter.toggle

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.selectable.connectView
import com.algolia.instantsearch.helper.filter.FilterPresenter
import com.algolia.instantsearch.helper.filter.FilterPresenterImpl
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState


public fun FilterToggleViewModel.connectView(
    view: FilterToggleView,
    presenter: FilterPresenter = FilterPresenterImpl()
): Connection {
    return connectView(view, presenter)
}

public fun FilterToggleViewModel.connectFilterState(
    filterState: FilterState,
    groupID: FilterGroupID = FilterGroupID(item.value.attribute, FilterOperator.And)
): Connection {
    return FilterToggleConnectionFilterState(this, filterState, groupID)
}

public fun FilterToggleConnector.connectView(
    view: FilterToggleView,
    presenter: FilterPresenter = FilterPresenterImpl()
): Connection {
    return viewModel.connectView(view, presenter)
}