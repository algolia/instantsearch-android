package com.algolia.instantsearch.helper.index

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.selectable.map.SelectableMapView
import com.algolia.instantsearch.core.selectable.map.connectView


public fun IndexSegmentViewModel.connectView(
    view: SelectableMapView<Int, String>,
    connect: Boolean = true,
    presenter: IndexPresenter
): Connection {
    return connectView(view, presenter, connect)
}