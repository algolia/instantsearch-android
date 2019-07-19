package com.algolia.instantsearch.core.number.range

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.autoConnect


public fun <T> NumberRangeViewModel<T>.connectView(
    view: NumberRangeView<T>,
    connect: Boolean = true
): Connection where T : Number, T : Comparable<T> {
    return NumberRangeConnectionView(this, view).autoConnect(connect)
}