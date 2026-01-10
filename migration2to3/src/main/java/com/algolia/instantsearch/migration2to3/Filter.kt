package com.algolia.instantsearch.migration2to3

public sealed class Filter {

    /**
     * The [Attribute] this filter applies on.
     */
    public abstract val attribute: Attribute

    /**
     * Whether or not the filter is negated.
     */
    public abstract val isNegated: Boolean

    /**
     * A [Filter.Facet] matches exactly an [attribute] with a [value].
     * An optional [score] allows to assign a priority between several [Filter.Facet] that are evaluated in the same
     * [FilterGroup].
     * [Read further on scoring][https://www.algolia.com/doc/guides/managing-results/refine-results/filtering/in-depth/filter-scoring/#filters-scoring]
     */
    public data class Facet internal constructor(
        override val attribute: Attribute,
        override val isNegated: Boolean,
        val value: Value,
        val score: Int? = null
    ) : Filter() {

        public sealed class Value {

            /**
             * Raw value.
             */
            public abstract val raw: Any

            /**
             * Filter on a [kotlin.String] value.
             */
            public data class String(override val raw: kotlin.String) : Value()

            /**
             * Filter on a [kotlin.Boolean] value.
             */
            public data class Boolean(override val raw: kotlin.Boolean) : Value()

            /**
             * Filter on a [kotlin.Number] value.
             */
            public data class Number(override val raw: kotlin.Number) : Value()
        }

        public constructor(
            attribute: Attribute,
            value: String,
            score: Int? = null,
            isNegated: Boolean = false
        ) : this(attribute, isNegated, Value.String(value), score)

        public constructor(
            attribute: Attribute,
            value: Boolean,
            score: Int? = null,
            isNegated: Boolean = false
        ) : this(attribute, isNegated, Value.Boolean(value), score)

        public constructor(
            attribute: Attribute,
            value: Number,
            score: Int? = null,
            isNegated: Boolean = false
        ) : this(attribute, isNegated, Value.Number(value), score)

        /**
         * Operator to negates a [Filter.Facet].
         */
        public operator fun not(): Facet {
            return Facet(attribute, true, value, score)
        }
    }

    /**
     * A [Filter.Tag] filters on a specific [value]. It uses a reserved keywords "_tags" as [attribute].
     */
    public data class Tag internal constructor(
        override val attribute: Attribute,
        override val isNegated: Boolean,
        val value: String
    ) : Filter() {

        public constructor(
            value: String,
            isNegated: Boolean = false
        ) : this(Attribute("_tags"), isNegated, value)

        /**
         * Operator to negates a [Filter.Tag].
         */
        public operator fun not(): Tag {
            return Tag(attribute, true, value)
        }
    }

    /**
     * A [Filter.Numeric] filters on a numeric [value].
     */
    public data class Numeric(
        override val attribute: Attribute,
        override val isNegated: Boolean,
        val value: Value
    ) : Filter() {

        public sealed class Value {

            /**
             * Numeric comparison of a [number] using a [NumericOperator].
             */
            public data class Comparison(val operator: NumericOperator, val number: Number) : Value()

            /**
             * A numeric range comprised within a [lowerBound] and an [upperBound].
             */
            public data class Range(val lowerBound: Number, val upperBound: Number) : Value()
        }

        public constructor(
            attribute: Attribute,
            operator: NumericOperator,
            value: Number,
            isNegated: Boolean = false
        ) : this(attribute, isNegated, Value.Comparison(operator, value))

        public constructor(
            attribute: Attribute,
            range: IntRange,
            isNegated: Boolean = false
        ) : this(attribute, isNegated, Value.Range(range.start, range.endInclusive))

        public constructor(
            attribute: Attribute,
            range: LongRange,
            isNegated: Boolean = false
        ) : this(attribute, isNegated, Value.Range(range.start, range.endInclusive))

        public constructor(
            attribute: Attribute,
            lowerBound: Float,
            upperBound: Float,
            isNegated: Boolean = false
        ) : this(attribute, isNegated, Value.Range(lowerBound, upperBound))

        public constructor(
            attribute: Attribute,
            lowerBound: Double,
            upperBound: Double,
            isNegated: Boolean = false
        ) : this(attribute, isNegated, Value.Range(lowerBound, upperBound))

        /**
         * Operator to negates a [Filter.Numeric].
         */
        public operator fun not(): Numeric {
            return Numeric(attribute, true, value)
        }
    }
}
