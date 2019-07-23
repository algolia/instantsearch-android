package com.algolia.instantsearch.core.number.range

import com.algolia.instantsearch.core.observable.SubscriptionEvent
import com.algolia.instantsearch.core.observable.SubscriptionValue


public open class NumberRangeViewModel<T>(
    range: Range<T>? = null,
    bounds: Range<T>? = null
) where T : Number, T : Comparable<T> {

    public val range = SubscriptionValue(range)
    public val bounds = SubscriptionValue(bounds).apply {
        subscribe { coerce(this@NumberRangeViewModel.range.value) }
    }
    public val eventRange = SubscriptionEvent<Range<T>?>()

    public fun coerce(range: Range<T>?) {
        val coerced = range?.coerce(bounds.value)

        if (coerced != this.range.value) eventRange.send(coerced)
    }
}