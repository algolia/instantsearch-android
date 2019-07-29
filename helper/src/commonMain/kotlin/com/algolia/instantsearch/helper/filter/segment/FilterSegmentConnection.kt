package com.algolia.instantsearch.helper.filter.segment

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.selectable.map.SelectableMapView
import com.algolia.instantsearch.core.selectable.map.connectView
import com.algolia.instantsearch.helper.filter.FilterPresenter
import com.algolia.instantsearch.helper.filter.FilterPresenterImpl
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState


public fun FilterSegmentViewModel.connectFilterState(
    filterState: FilterState,
    groupID: FilterGroupID = FilterGroupID(FilterOperator.And)
): Connection {
    return FilterSegmentConnectionFilterState(this, filterState, groupID)
}

public fun FilterSegmentViewModel.connectView(
    view: SelectableMapView<Int, String>,
    presenter: FilterPresenter = FilterPresenterImpl()
): Connection {
    return connectView(view, presenter)
}

public fun FilterSegmentWidget.connectView(
    view: SelectableMapView<Int, String>,
    presenter: FilterPresenter = FilterPresenterImpl()
): Connection {
    return viewModel.connectView(view, presenter)
}