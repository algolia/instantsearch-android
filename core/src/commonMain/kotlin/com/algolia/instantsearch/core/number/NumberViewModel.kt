package com.algolia.instantsearch.core.number

import com.algolia.instantsearch.core.number.range.Range
import com.algolia.instantsearch.core.number.range.coerce
import com.algolia.instantsearch.core.observable.ObservableEvent
import com.algolia.instantsearch.core.observable.ObservableItem


public open class NumberViewModel<T>(
    number: T? = null,
    bounds: Range<T>? = null
) where T : Number, T : Comparable<T> {

    public val number = ObservableItem(number)

    public val event = ObservableEvent<T?>()
    public val bounds = ObservableItem(bounds).apply {
        subscribe { coerce(this@NumberViewModel.number.get()) }
    }

    public fun coerce(number: T?) {
        val coerced = number?.coerce(bounds.get())

        if (coerced != this.number.get()) event.send(coerced)
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