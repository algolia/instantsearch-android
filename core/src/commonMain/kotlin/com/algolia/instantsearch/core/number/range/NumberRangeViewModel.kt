package com.algolia.instantsearch.core.number.range

import com.algolia.instantsearch.core.number.Range
import kotlin.properties.Delegates


public abstract class NumberRangeViewModel<T : Number>(range: Range<T>) {

    val onRangeChanged: MutableList<(Range<T>) -> Unit> = mutableListOf()

    public var range by Delegates.observable(range) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            onRangeChanged.forEach { it(newValue) }
        }
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