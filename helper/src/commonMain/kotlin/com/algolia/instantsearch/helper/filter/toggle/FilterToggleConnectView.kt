package com.algolia.instantsearch.helper.filter.toggle

import com.algolia.instantsearch.core.selectable.SelectableItemView
import com.algolia.instantsearch.helper.filter.FilterPresenterImpl
import com.algolia.search.model.filter.Filter


public fun FilterToggleViewModel.connectView(
    view: SelectableItemView,
    presenter: (Filter) -> String = FilterPresenterImpl()
) {
    view.setItem(presenter(item))
    view.setIsSelected(isSelected)
    view.onClick = (::computeIsSelected)
    onIsSelectedChanged += (view::setIsSelected)
}