package com.algolia.instantsearch.core.number.range

import com.algolia.instantsearch.core.number.Range


public fun <T: Number> NumberRangeViewModel<T>.connectView(view: NumberRangeView<T>) {
    val onRangeChange: (Range<T>) -> Unit = { view.setItem(it) }

    onRangeChange(item)
    onItemChanged += onRangeChange
    view.onNewRange = { newRange -> item = newRange }
}
