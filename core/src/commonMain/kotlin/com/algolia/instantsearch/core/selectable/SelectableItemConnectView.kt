package com.algolia.instantsearch.core.selectable

import com.algolia.instantsearch.core.Presenter


public fun <I, O> SelectableItemViewModel<I>.connectView(
    view: SelectableItemView<O>,
    presenter: Presenter<I, O>
) {
    view.setItem(presenter(item))
    view.setIsSelected(isSelected)
    view.onClick = (::computeIsSelected)
    onIsSelectedChanged += (view::setIsSelected)
}