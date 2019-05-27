package com.algolia.instantsearch.helper.filter

import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator


interface FilterPresenter : (Filter) -> String {

    val facetString: (Attribute, String, Boolean) -> String
    val facetBoolean: (Attribute, Boolean, Boolean) -> String
    val facetNumber: (Attribute, Number, Boolean) -> String
    val tag: (Attribute, String, Boolean) -> String
    val numericComparison: (Attribute, NumericOperator, Number, Boolean) -> String
    val numericRange: (Attribute, Number, Number, Boolean) -> String

    override fun invoke(filter: Filter): String {
        return when (filter) {
            is Filter.Facet -> {
                when (val value = filter.value) {
                    is Filter.Facet.Value.String -> facetString(filter.attribute, value.raw, filter.isNegated)
                    is Filter.Facet.Value.Number -> facetNumber(filter.attribute, value.raw, filter.isNegated)
                    is Filter.Facet.Value.Boolean -> facetBoolean(filter.attribute, value.raw, filter.isNegated)
                }
            }
            is Filter.Tag -> tag(filter.attribute, filter.value, filter.isNegated)
            is Filter.Numeric -> when (val value = filter.value) {
                is Filter.Numeric.Value.Comparison -> {
                    numericComparison(filter.attribute, value.operator, value.number, filter.isNegated)
                }
                is Filter.Numeric.Value.Range -> {
                    numericRange(filter.attribute, value.lowerBound, value.upperBound, filter.isNegated)
                }
            }
        }
    }
}