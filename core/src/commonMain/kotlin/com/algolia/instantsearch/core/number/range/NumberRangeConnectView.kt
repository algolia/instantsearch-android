package com.algolia.instantsearch.core.number.range

import com.algolia.instantsearch.core.item.connectView


public fun <T> NumberRangeViewModel<T>.connectView(
    view: NumberRangeView<T>
) where T : Number, T : Comparable<T> {
    bounds.subscribePast { view.setBounds(it) }
    range.connectView(view) { it }
    view.onClick = (::coerce)
}