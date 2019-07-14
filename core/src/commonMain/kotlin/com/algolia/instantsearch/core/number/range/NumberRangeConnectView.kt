package com.algolia.instantsearch.core.number.range

import com.algolia.instantsearch.core.item.connectView
import com.algolia.instantsearch.core.observable.ObservableKey


public fun <T> NumberRangeViewModel<T>.connectView(
    view: NumberRangeView<T>,
    key: ObservableKey? = null
) where T : Number, T : Comparable<T> {
    bounds.subscribePast { view.setBounds(it) }
    range.connectView(view, key) { it }
    view.onClick = (::coerce)
}