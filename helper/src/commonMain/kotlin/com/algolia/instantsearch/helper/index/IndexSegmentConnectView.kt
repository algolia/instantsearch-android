package com.algolia.instantsearch.helper.index

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.selectable.map.SelectableMapView
import com.algolia.instantsearch.core.selectable.map.connectView


public fun IndexSegmentViewModel.connectView(
    view: SelectableMapView<Int, String>,
    presenter: IndexPresenter,
    connect: Boolean = true
): Connection {
    return connectView(view, presenter, connect)
}