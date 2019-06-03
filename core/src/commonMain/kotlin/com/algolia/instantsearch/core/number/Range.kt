package com.algolia.instantsearch.core.number


public abstract class Range<T : Number> {

    public abstract val min: T
    public abstract val max: T

    public data class Int(
        override val min: kotlin.Int,
        override val max: kotlin.Int
    ) : Range<kotlin.Int>() {

        public constructor(range: IntRange): this(range.first, range.last)

        init {
            if (min > max) throw IllegalArgumentException("min $min is greater than max $max.")
        }
    }

    public data class Long(
        override val min: kotlin.Long,
        override val max: kotlin.Long
    ) : Range<kotlin.Long>() {

        public constructor(range: LongRange): this(range.first, range.last)

        init {
            if (min > max) throw IllegalArgumentException("min $min is greater than max $max.")
        }
    }

    public data class Float(
        override val min: kotlin.Float,
        override val max: kotlin.Float
    ) : Range<kotlin.Float>() {

        init {
            if (min > max) throw IllegalArgumentException("min $min is greater than max $max.")
        }
    }

    public data class Double(
        override val min: kotlin.Double,
        override val max: kotlin.Double
    ) : Range<kotlin.Double>() {

        init {
            if (min > max) throw IllegalArgumentException("min $min is greater than max $max.")
        }
    }
}