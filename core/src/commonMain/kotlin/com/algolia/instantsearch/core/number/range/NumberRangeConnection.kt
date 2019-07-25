package com.algolia.instantsearch.core.number.range

import com.algolia.instantsearch.core.connection.Connection


public fun <T> NumberRangeViewModel<T>.connectionView(
    view: NumberRangeView<T>
): Connection where T : Number, T : Comparable<T> {
    return NumberRangeConnectionView(this, view)
}