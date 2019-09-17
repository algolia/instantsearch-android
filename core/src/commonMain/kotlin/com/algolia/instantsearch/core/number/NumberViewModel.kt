package com.algolia.instantsearch.core.number

import com.algolia.instantsearch.core.number.range.Range
import com.algolia.instantsearch.core.number.range.coerce
import com.algolia.instantsearch.core.subscription.SubscriptionEvent
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import kotlin.jvm.JvmField
import kotlin.jvm.JvmOverloads


public open class NumberViewModel<T> @JvmOverloads constructor(
    number: T? = null,
    bounds: Range<T>? = null
) where T : Number, T : Comparable<T> {

    @JvmField
    public val eventNumber = SubscriptionEvent<T?>()
    @JvmField
    public val number = SubscriptionValue(number)
    @JvmField
    public val bounds = SubscriptionValue(bounds).apply {
        subscribe { coerce(this@NumberViewModel.number.value) }
    }

    public fun coerce(number: T?) {
        val coerced = number?.coerce(bounds.value)

        if (coerced != this.number.value) eventNumber.send(coerced)
    }

    companion object {

        public operator fun <T> invoke(
            range: ClosedRange<T>,
            number: T? = null
        ): NumberViewModel<T> where T : Number, T : Comparable<T> {
            return NumberViewModel(number, Range(range.start, range.endInclusive))
        }
    }
}