package com.algolia.instantsearch.helper.filter.numeric.comparison

import com.algolia.instantsearch.core.number.NumberView
import com.algolia.instantsearch.core.number.NumberViewModel


public inline fun <reified T : Number> NumberViewModel<T>.connectView(
    view: NumberView<T>
) {
    view.setComputation { computeNumber(it(item)) }
    view.setItem(item)
    onItemChanged += (view::setItem)
}