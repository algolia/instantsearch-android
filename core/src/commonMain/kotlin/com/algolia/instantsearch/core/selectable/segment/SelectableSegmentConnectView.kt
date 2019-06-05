package com.algolia.instantsearch.core.selectable.segment

import com.algolia.instantsearch.core.Presenter


public fun <K, I, O> SelectableSegmentViewModel<K, I>.connectView(
    view: SelectableSegmentView<K, O>,
    presenter: Presenter<I, O>
) {

    fun Map<K, I>.present(): Map<K, O> {
        return map { it.key to presenter(it.value) }.toMap()
    }

    view.setItem(item.present())
    view.setSelected(selected)
    view.onClick = (::computeSelected)
    onItemChanged += { view.setItem(item.present()) }
    onSelectedChanged += (view::setSelected)
}