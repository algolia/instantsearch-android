package com.algolia.instantsearch.core.number.range

import com.algolia.instantsearch.core.event.EventViewModel
import com.algolia.instantsearch.core.event.EventViewModelImpl
import com.algolia.instantsearch.core.item.ItemViewModel
import com.algolia.instantsearch.core.number.Range


public abstract class NumberRangeViewModel<T : Number>(
    bounds: Range<T>? = null,
    private val coerce: (Range<T>?, Range<T>?) -> Range<T>?
) : ItemViewModel<Range<T>?>(null),
    EventViewModel<Range<T>> by EventViewModelImpl<Range<T>>() {

    public val onRangeComputed: MutableList<(Range<T>?) -> Unit> = mutableListOf()

    public var range: Range<T>?
        get() = item
        set(value) {
            item = computeRange(value)
        }

    public var bounds: Range<T>? = bounds
        set(value) {
            field = value
            value?.let {
                computeRange()
            }
        }

    public fun computeRange(range: Range<T>? = this.range): Range<T>? {
        val coerced = coerce(range, bounds)

        if (coerced != item) onRangeComputed.forEach { it(coerced) }
        return coerced
    }

    public class Int(
        range: Range.Int
    ) : NumberRangeViewModel<kotlin.Int>(range) {

        public constructor(range: IntRange) : this(Range.Int(range))
    }

    public class Long(
        range: Range.Long
    ) : NumberRangeViewModel<kotlin.Long>(range) {

        public constructor(range: LongRange) : this(Range.Long(range))
    }

    public class Float(
        range: Range.Float
    ) : NumberRangeViewModel<kotlin.Float>(range)

    public class Double(
        range: Range.Double
    ) : NumberRangeViewModel<kotlin.Double>(range)
}