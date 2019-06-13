package com.algolia.instantsearch.core.number.range

import com.algolia.instantsearch.core.item.ItemViewModel
import com.algolia.instantsearch.core.number.Range


public abstract class NumberRangeViewModel<T : Number>(range: Range<T>) : ItemViewModel<Range<T>>(range) {

    public var range: Range<T>
        get() = item
        set(value) {
            item = value
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