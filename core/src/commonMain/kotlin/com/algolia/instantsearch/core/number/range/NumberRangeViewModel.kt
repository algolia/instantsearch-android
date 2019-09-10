package com.algolia.instantsearch.core.number.range

import com.algolia.instantsearch.core.subscription.SubscriptionEvent
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import kotlin.jvm.JvmField


public open class NumberRangeViewModel<T>(
    range: Range<T>? = null,
    bounds: Range<T>? = null
) where T : Number, T : Comparable<T> {

    @JvmField
    public val range = SubscriptionValue(range)
    @JvmField
    public val bounds = SubscriptionValue(bounds).apply {
        subscribe { coerce(this@NumberRangeViewModel.range.value) }
    }
    @JvmField
    public val eventRange = SubscriptionEvent<Range<T>?>()

    public fun coerce(range: Range<T>?) {
        val coerced = range?.coerce(bounds.value)

        if (coerced != this.range.value) eventRange.send(coerced)
    }
}