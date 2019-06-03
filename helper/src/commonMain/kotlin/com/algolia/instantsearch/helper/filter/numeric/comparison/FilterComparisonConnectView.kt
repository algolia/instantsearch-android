package com.algolia.instantsearch.helper.filter.numeric.comparison

import com.algolia.instantsearch.core.selectable.number.SelectableNumberView
import com.algolia.instantsearch.core.selectable.number.SelectableNumberViewModel


public inline fun <reified T : Number> SelectableNumberViewModel<T>.connectView(
    view: SelectableNumberView<T>
) {
    view.setComputation { computeNumber(it(item)) }
    view.setItem(item)
    onItemChanged += (view::setItem)
}