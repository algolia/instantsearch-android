package com.algolia.instantsearch.core.number.range

import com.algolia.instantsearch.core.event.connectView as connectEventView
import com.algolia.instantsearch.core.item.connectView as connectItemView


public fun <T> NumberRangeViewModel<T>.connectView(
    view: NumberRangeView<T>
) where T : Number, T : Comparable<T> {
    connectRangeView(view)
    connectItemView(view) { it }
    connectEventView(view) { trigger(it) }
}

private fun <T> NumberRangeViewModel<T>.connectRangeView(
    view: NumberRangeView<T>
) where T : Number, T : Comparable<T> {
    val onBoundsComputed: (Range<T>?) -> Unit = { view.setBounds(it) }

    this.onBoundsComputed += onBoundsComputed
    onBoundsComputed(bounds)
}