package com.algolia.instantsearch.helper.filter.segment

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.selectable.map.SelectableMapView
import com.algolia.instantsearch.core.selectable.map.connectionView
import com.algolia.instantsearch.helper.filter.FilterPresenter
import com.algolia.instantsearch.helper.filter.FilterPresenterImpl
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState


public fun FilterSegmentViewModel.connectionFilterState(
    filterState: FilterState,
    groupID: FilterGroupID
): Connection {
    return FilterSegmentConnectionFilterState(this, filterState, groupID)
}

public fun FilterSegmentViewModel.connectionView(
    view: SelectableMapView<Int, String>,
    presenter: FilterPresenter = FilterPresenterImpl()
): Connection {
    return connectionView(view, presenter)
}

public fun FilterSegmentWidget.connectionView(
    view: SelectableMapView<Int, String>,
    presenter: FilterPresenter = FilterPresenterImpl()
): Connection {
    return viewModel.connectionView(view, presenter)
}