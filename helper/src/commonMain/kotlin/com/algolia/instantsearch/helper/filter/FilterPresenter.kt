package com.algolia.instantsearch.helper.filter

import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator


interface FilterPresenter : (Filter) -> String {

    fun facet(attribute: Attribute, value: String, isNegated: Boolean): String {
        return value
    }

    fun facet(attribute: Attribute, value: Boolean, isNegated: Boolean): String {
        return attribute.raw
    }

    fun facet(attribute: Attribute, value: Number, isNegated: Boolean): String {
        return "${attribute.raw}: $value"
    }

    fun tag(value: String, isNegated: Boolean): String {
        return value
    }

    fun numeric(attribute: Attribute, operator: NumericOperator, value: Number, isNegated: Boolean): String {
        return "$attribute ${operator.raw} $value"
    }

    fun numeric(attribute: Attribute, lowerBound: Number, upperBound: Number, isNegated: Boolean): String {
        return "$attribute: $lowerBound to $upperBound"
    }

    override fun invoke(filter: Filter): String {
        return when (filter) {
            is Filter.Facet -> {
                when (val value = filter.value) {
                    is Filter.Facet.Value.String -> facet(filter.attribute, value.raw, filter.isNegated)
                    is Filter.Facet.Value.Number -> facet(filter.attribute, value.raw, filter.isNegated)
                    is Filter.Facet.Value.Boolean -> facet(filter.attribute, value.raw, filter.isNegated)
                }
            }
            is Filter.Tag -> tag(filter.value, filter.isNegated)
            is Filter.Numeric -> when (val value = filter.value) {
                is Filter.Numeric.Value.Comparison -> {
                    numeric(filter.attribute, value.operator, value.number, filter.isNegated)
                }
                is Filter.Numeric.Value.Range -> {
                    numeric(filter.attribute, value.lowerBound, value.upperBound, filter.isNegated)
                }
            }
        }
    }
}