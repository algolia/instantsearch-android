package com.algolia.instantsearch.core.selectable.segment

import com.algolia.instantsearch.core.Presenter


public fun <K, V, O> SelectableSegmentViewModel<K, V>.connectView(
    view: SelectableSegmentView<K, O>,
    presenter: Presenter<V, O>
) {
    view.setItem(item.map { it.key to presenter(it.value) }.toMap())
    view.setSelected(selected)
    view.onClick = (::computeSelected)
    onSelectedChanged += (view::setSelected)
}