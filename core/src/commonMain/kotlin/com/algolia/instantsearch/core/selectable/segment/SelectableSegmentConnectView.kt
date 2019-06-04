package com.algolia.instantsearch.core.selectable.segment

import com.algolia.instantsearch.core.Presenter


public fun <K, I, O> SelectableSegmentViewModel<K, I>.connectView(
    view: SelectableSegmentView<K, O>,
    presenter: Presenter<I, O>
) {
    view.setItem(item.map { it.key to presenter(it.value) }.toMap())
    view.setSelected(selected)
    view.onClick = (::computeSelected)
    onSelectedChanged += (view::setSelected)
}