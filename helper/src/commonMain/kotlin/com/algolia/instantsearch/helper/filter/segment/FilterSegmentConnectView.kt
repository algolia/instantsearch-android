package com.algolia.instantsearch.helper.filter.segment

import com.algolia.instantsearch.helper.filter.FilterPresenterImpl
import com.algolia.search.model.filter.Filter


public fun FilterSegmentViewModel.connectView(
    view: FilterSegmentView,
    presenter: (Filter) -> String = FilterPresenterImpl
) {
    view.setItems(items.map { it.key to presenter(it.value) }.toMap())
    view.setSelected(selected)
    view.onClick = (::computeSelected)
    onSelectedChanged += (view::setSelected)
}