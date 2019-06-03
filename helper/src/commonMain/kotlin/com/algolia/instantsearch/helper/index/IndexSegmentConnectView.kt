package com.algolia.instantsearch.helper.index

import com.algolia.instantsearch.core.selectable.segment.SelectableSegmentView


public fun IndexSegmentViewModel.connectView(
    view: SelectableSegmentView<Int, String>,
    presenter: IndexPresenter
) {
    view.setItem(item.map { it.key to presenter(it.value) }.toMap())
    view.setSelected(selected)
    view.onClick = (::computeSelected)
    onSelectedChanged += (view::setSelected)
}