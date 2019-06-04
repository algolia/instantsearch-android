package com.algolia.instantsearch.helper.filter

import com.algolia.instantsearch.core.Presenter
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator


public interface FilterPresenter : Presenter<Filter, String> {

    public val facetString: (Attribute, String, Boolean) -> String
    public val facetBoolean: (Attribute, Boolean, Boolean) -> String
    public val facetNumber: (Attribute, Number, Boolean) -> String
    public val tag: (Attribute, String, Boolean) -> String
    public val numericComparison: (Attribute, NumericOperator, Number, Boolean) -> String
    public val numericRange: (Attribute, Number, Number, Boolean) -> String

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