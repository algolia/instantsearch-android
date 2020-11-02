package com.algolia.instantsearch.core.number.range

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.number.range.internal.NumberRangeConnectionView

public fun <T> NumberRangeViewModel<T>.connectView(
    view: NumberRangeView<T>
): Connection where T : Number, T : Comparable<T> {
    return NumberRangeConnectionView(this, view)
}
