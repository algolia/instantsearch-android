package com.algolia.instantsearch.helper.filter.segment

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.selectable.map.SelectableMapView
import com.algolia.instantsearch.core.selectable.map.connectView
import com.algolia.instantsearch.helper.filter.FilterPresenter
import com.algolia.instantsearch.helper.filter.FilterPresenterImpl


public fun FilterSegmentViewModel.connectView(
    view: SelectableMapView<Int, String>,
    presenter: FilterPresenter = FilterPresenterImpl(),
    connect: Boolean = true
): Connection {
    return connectView(view, presenter, connect)
}