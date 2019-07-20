package com.algolia.instantsearch.helper.index

import com.algolia.instantsearch.core.selectable.map.SelectableMapView
import com.algolia.instantsearch.core.selectable.map.connectView


public fun IndexSegmentViewModel.connectView(
    view: SelectableMapView<Int, String>,
    presenter: IndexPresenter
) {
    connectView(view, presenter)
}