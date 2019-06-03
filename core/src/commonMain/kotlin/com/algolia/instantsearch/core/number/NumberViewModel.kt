package com.algolia.instantsearch.core.number

import com.algolia.instantsearch.core.item.ItemViewModel


public abstract class NumberViewModel<T : Number>(
    bounds: Range<T>?,
    private val comparator: Comparator<T>,
    private val coerce: T.(Range<T>?) -> (T)
): ItemViewModel<T?>(null) {

    public val onNumberComputed: MutableList<(T?) -> Unit> = mutableListOf()

    public var bounds: Range<T>? = bounds
        private set

    public fun applyBounds(bounds: Range<T>?) {
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
    ) : NumberViewModel<kotlin.Int>(
        bounds,
        Comparator { a, b -> a.compareTo(b) },
        { range -> range?.let { coerceIn(it.min, it.max) } ?: this }
    ) {

        public constructor(range: IntRange) : this(Range.Int(range))
    }

    public class Long(
        bounds: Range.Long? = null
    ) : NumberViewModel<kotlin.Long>(
        bounds,
        Comparator { a, b -> a.compareTo(b) },
        { range -> range?.let { coerceIn(it.min, it.max) } ?: this }
    ) {

        public constructor(range: LongRange) : this(Range.Long(range))
    }

    public class Float(
        bounds: Range.Float? = null
    ) : NumberViewModel<kotlin.Float>(
        bounds,
        Comparator { a, b -> a.compareTo(b) },
        { range -> range?.let { coerceIn(it.min, it.max) } ?: this }
    )

    public class Double(
        bounds: Range.Double? = null
    ) : NumberViewModel<kotlin.Double>(
        bounds,
        Comparator { a, b -> a.compareTo(b) },
        { range -> range?.let { coerceIn(it.min, it.max) } ?: this }
    )
}