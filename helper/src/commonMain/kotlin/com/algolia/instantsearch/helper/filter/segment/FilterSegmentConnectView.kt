package com.algolia.instantsearch.helper.filter.segment

import com.algolia.instantsearch.core.selectable.segment.SelectableSegmentView
import com.algolia.instantsearch.core.selectable.segment.connectView
import com.algolia.instantsearch.helper.filter.FilterPresenter
import com.algolia.instantsearch.helper.filter.FilterPresenterImpl


public fun FilterSegmentViewModel.connectView(
    view: SelectableSegmentView<Int, String>,
    presenter: FilterPresenter = FilterPresenterImpl()
) {
    connectView(view, presenter)
}