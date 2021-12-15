package com.algolia.instantsearch.core.number.range

import com.algolia.instantsearch.core.internal.traceNumberRangeFilter
import com.algolia.instantsearch.core.number.range.internal.coerce
import com.algolia.instantsearch.core.subscription.SubscriptionEvent
import com.algolia.instantsearch.core.subscription.SubscriptionValue

public open class NumberRangeViewModel<T>(
    range: Range<T>? = null,
    bounds: Range<T>? = null,
) where T : Number, T : Comparable<T> {

    public val range: SubscriptionValue<Range<T>?> = SubscriptionValue(range)
    public val bounds: SubscriptionValue<Range<T>?> = SubscriptionValue(bounds).apply {
        subscribe { coerce(this@NumberRangeViewModel.range.value) }
    }
    public val eventRange: SubscriptionEvent<Range<T>?> = SubscriptionEvent()

    init {
        traceNumberRangeFilter(range != null, bounds != null)
    }

    public fun coerce(range: Range<T>?) {
        val coerced = range?.coerce(bounds.value)

        if (coerced != this.range.value) eventRange.send(coerced)
    }
}
