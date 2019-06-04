package com.algolia.instantsearch.helper.index

import com.algolia.instantsearch.core.selectable.segment.SelectableSegmentView
import com.algolia.instantsearch.core.selectable.segment.connectView


public fun IndexSegmentViewModel.connectView(
    view: SelectableSegmentView<Int, String>,
    presenter: IndexPresenter
) {
    connectView(view, presenter)
}