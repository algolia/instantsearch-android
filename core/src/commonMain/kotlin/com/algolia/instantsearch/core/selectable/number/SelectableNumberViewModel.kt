package com.algolia.instantsearch.core.selectable.number

import com.algolia.instantsearch.core.item.ItemViewModel
import com.algolia.instantsearch.core.selectable.range.Range


public abstract class SelectableNumberViewModel<T : Number>(
    bounds: Range<T>?,
    private val comparator: Comparator<T>,
    private val coerce: T.(Range<T>?) -> (T)
): ItemViewModel<T>() {

    public val onNumberComputed: MutableList<(T?) -> Unit> = mutableListOf()

    public var bounds: Range<T>? = bounds
        private set

    public fun computeBounds(bounds: Range<T>?) {
        val coerced = item?.let { coerce(it, bounds) }

        this.bounds = bounds
        if (coerced != item) onNumberComputed.forEach { it(coerced) }
    }

    public fun computeNumber(number: T?) {
        val coerced = number?.let { coerce(it, bounds) }

        onNumberComputed.forEach { it(coerced) }
    }

    private operator fun T.compareTo(other: T): kotlin.Int {
        return comparator.compare(this, other)
    }

    public class Int(
        bounds: Range.Int? = null
    ) : SelectableNumberViewModel<kotlin.Int>(
        bounds,
        Comparator { a, b -> a.compareTo(b) },
        { range -> range?.let { coerceIn(it.min, it.max) } ?: this }
    ) {

        public constructor(range: IntRange) : this(Range.Int(range))
    }

    public class Long(
        bounds: Range.Long? = null
    ) : SelectableNumberViewModel<kotlin.Long>(
        bounds,
        Comparator { a, b -> a.compareTo(b) },
        { range -> range?.let { coerceIn(it.min, it.max) } ?: this }
    ) {

        public constructor(range: LongRange) : this(Range.Long(range))
    }

    public class Float(
        bounds: Range.Float? = null
    ) : SelectableNumberViewModel<kotlin.Float>(
        bounds,
        Comparator { a, b -> a.compareTo(b) },
        { range -> range?.let { coerceIn(it.min, it.max) } ?: this }
    )

    public class Double(
        bounds: Range.Double? = null
    ) : SelectableNumberViewModel<kotlin.Double>(
        bounds,
        Comparator { a, b -> a.compareTo(b) },
        { range -> range?.let { coerceIn(it.min, it.max) } ?: this }
    )
}