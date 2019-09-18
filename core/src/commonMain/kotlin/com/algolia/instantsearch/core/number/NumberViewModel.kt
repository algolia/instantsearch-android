package com.algolia.instantsearch.core.number

import com.algolia.instantsearch.core.number.range.Range
import com.algolia.instantsearch.core.number.range.coerce
import com.algolia.instantsearch.core.subscription.SubscriptionEvent
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import kotlin.jvm.JvmField
import kotlin.jvm.JvmOverloads

/**
 * A ViewModel storing a number, that might be bounded.
 *
 * @param number the number's initial value.
 * @param bounds optional bounds coercing the number.
 */
public open class NumberViewModel<T> @JvmOverloads constructor(
    number: T? = null,
    bounds: Range<T>? = null
) where T : Number, T : Comparable<T> {

    /**
     * Event fired whenever the number changes.
     */
    @JvmField
    public val eventNumber = SubscriptionEvent<T?>()
    @JvmField
    public val number = SubscriptionValue(number)
    /**
     * Optional bounds that coerce the number.
     */
    @JvmField
    public val bounds = SubscriptionValue(bounds).apply {
        subscribe { coerce(this@NumberViewModel.number.value) }
    }

    /**
     * Coerces a number within this ViewModel's [bounds].
     *
     * @param number a value to coerce.
     */
    public fun coerce(number: T?) {
        val coerced = number?.coerce(bounds.value)

        if (coerced != this.number.value) eventNumber.send(coerced)
    }

    companion object {
        //TODO Document and test
        public operator fun <T> invoke(
            range: ClosedRange<T>,
            number: T? = null
        ): NumberViewModel<T> where T : Number, T : Comparable<T> {
            return NumberViewModel(number, Range(range.start, range.endInclusive))
        }
    }
}