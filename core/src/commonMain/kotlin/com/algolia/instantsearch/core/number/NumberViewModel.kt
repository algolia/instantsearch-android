package com.algolia.instantsearch.core.number

import com.algolia.instantsearch.core.item.ItemViewModel
import com.algolia.instantsearch.core.number.range.Range
import com.algolia.instantsearch.core.number.range.coerce


public open class NumberViewModel<T>(
    bounds: Range<T>? = null
) : ItemViewModel<T?>(null) where T : Number, T : Comparable<T> {

    public val onNumberComputed: MutableList<(T?) -> Unit> = mutableListOf()

    public var bounds: Range<T>? = bounds
        set(value) {
            val coerced = item?.coerce(value)

            field = value
            if (coerced != item) onNumberComputed.forEach { it(coerced) }
        }

    public fun computeNumber(number: T?) {
        val coerced = number?.coerce(bounds)

        onNumberComputed.forEach { it(coerced) }
    }

    companion object {

        public operator fun <T> invoke(range: ClosedRange<T>): NumberViewModel<T> where T : Number, T : Comparable<T> {
            return NumberViewModel(Range(range.start, range.endInclusive))
        }
    }
}