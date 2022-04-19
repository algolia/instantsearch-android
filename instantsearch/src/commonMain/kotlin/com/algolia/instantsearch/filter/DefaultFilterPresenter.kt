package com.algolia.instantsearch.filter

import com.algolia.instantsearch.attribute.AttributePresenter
import com.algolia.instantsearch.attribute.DefaultAttributePresenter
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.NumericOperator

public class DefaultFilterPresenter(
    private val attributePresenter: AttributePresenter = DefaultAttributePresenter(),
    override val facetString: (Attribute, String, Boolean) -> String = { _, value, _ ->
        value
    },
    override val facetBoolean: (Attribute, Boolean, Boolean) -> String = { attribute, _, _ ->
        attributePresenter(attribute)
    },
    override val facetNumber: (Attribute, Number, Boolean) -> String = { attribute, value, _ ->
        "${attributePresenter(attribute)}: $value"
    },
    override val tag: (Attribute, String, Boolean) -> String = { _, value, _ -> value },
    override val numericComparison: (Attribute, NumericOperator, Number, Boolean) -> String = { attribute, operator, value, _ ->
        "${attributePresenter(attribute)} ${operator.raw} $value"
    },
    override val numericRange: (Attribute, Number, Number, Boolean) -> String = { attribute, lowerBound, upperBound, _ ->
        "${attributePresenter(attribute)}: $lowerBound to $upperBound"
    },
) : FilterPresenter
