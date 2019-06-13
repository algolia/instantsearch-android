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
        bounds: Range.Int? = null
    ) : NumberRangeViewModel<kotlin.Int>(
        bounds,
        { range: Range<kotlin.Int>?, bounds: Range<kotlin.Int>? -> range?.coerceIn(bounds) }
    ) {

        public constructor(range: IntRange) : this(Range.Int(range))
    }

    public class Long(
        bounds: Range.Long? = null
    ) : NumberRangeViewModel<kotlin.Long>(
        bounds,
        { range: Range<kotlin.Long>?, bounds: Range<kotlin.Long>? -> range?.coerceIn(bounds) }
    ) {

        public constructor(range: LongRange) : this(Range.Long(range))
    }

    public class Float(
        bounds: Range.Float? = null
    ) : NumberRangeViewModel<kotlin.Float>(
        bounds,
        { range: Range<kotlin.Float>?, bounds: Range<kotlin.Float>? -> range?.coerceIn(bounds) }
    )

    public class Double(
        bounds: Range.Double? = null
    ) : NumberRangeViewModel<kotlin.Double>(
        bounds,
        { range: Range<kotlin.Double>?, bounds: Range<kotlin.Double>? -> range?.coerceIn(bounds) }
    )
}