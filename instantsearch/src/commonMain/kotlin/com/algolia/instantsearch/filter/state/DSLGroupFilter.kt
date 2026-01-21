package com.algolia.instantsearch.filter.state

import com.algolia.instantsearch.filter.Filter
import com.algolia.instantsearch.filter.NumericOperator

public interface DSL<T, R> {
    public operator fun invoke(block: T.() -> Unit): R
}

public class DSLGroupFilter internal constructor() : LinkedHashSet<Filter>() {
    public constructor(block: DSLGroupFilter.() -> Unit) : this() {
        block()
    }

    public operator fun Filter.unaryPlus() {
        add(this)
    }

    public fun filter(value: Filter) {
        add(value)
    }

    public fun facet(
        attribute: String,
        value: String,
        score: Int? = null,
        isNegated: Boolean = false,
    ) {
        add(Filter.Facet(attribute = attribute, value = value, score = score, isNegated = isNegated))
    }

    public fun tag(
        value: String,
        isNegated: Boolean = false,
    ) {
        add(Filter.Tag(value = value, isNegated = isNegated))
    }

    public fun comparison(
        attribute: String,
        operator: NumericOperator,
        value: Number,
        isNegated: Boolean = false,
    ) {
        add(Filter.Numeric(attribute = attribute, operator = operator, value = value, isNegated = isNegated))
    }

    public fun range(
        attribute: String,
        range: IntRange,
        isNegated: Boolean = false,
    ) {
        add(Filter.Numeric(attribute = attribute, range = range, isNegated = isNegated))
    }

    public fun range(
        attribute: String,
        range: LongRange,
        isNegated: Boolean = false,
    ) {
        add(Filter.Numeric(attribute = attribute, range = range, isNegated = isNegated))
    }
}
