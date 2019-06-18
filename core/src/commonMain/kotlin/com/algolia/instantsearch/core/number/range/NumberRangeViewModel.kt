package com.algolia.instantsearch.core.number.range

import com.algolia.instantsearch.core.event.EventViewModel
import com.algolia.instantsearch.core.event.EventViewModelImpl
import com.algolia.instantsearch.core.item.ItemViewModel


public open class NumberRangeViewModel<T>(
    bounds: Range<T>? = null
) : ItemViewModel<Range<T>?>(null),
    EventViewModel<Range<T>> by EventViewModelImpl<Range<T>>()
        where T : Number, T : Comparable<T> {

    public val onRangeComputed: MutableList<(Range<T>?) -> Unit> = mutableListOf()

    public var bounds: Range<T>? = bounds
        set(value) {
            field = value
            computeRange(item)
        }

    public fun computeRange(range: Range<T>?) {
        val coerced = range?.coerce(bounds)

        if (coerced != item) onRangeComputed.forEach { it(coerced) }
    }
}