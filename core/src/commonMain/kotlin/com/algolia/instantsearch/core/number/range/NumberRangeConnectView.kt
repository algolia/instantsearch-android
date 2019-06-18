package com.algolia.instantsearch.core.number.range

import com.algolia.instantsearch.core.event.connectView as connectEventView
import com.algolia.instantsearch.core.item.connectView as connectItemView


public fun <T> NumberRangeViewModel<T>.connectView(
    view: NumberRangeView<T>
) where T : Number, T : Comparable<T> {
    connectItemView(view) { it }
    connectEventView(view) { item?.let { trigger(it) } }
}