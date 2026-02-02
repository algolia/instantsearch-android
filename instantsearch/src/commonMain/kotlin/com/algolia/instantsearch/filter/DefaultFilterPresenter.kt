package com.algolia.instantsearch.filter

import com.algolia.instantsearch.attribute.AttributePresenter
import com.algolia.instantsearch.attribute.DefaultAttributePresenter

public class DefaultFilterPresenter(
    private val attributePresenter: AttributePresenter = DefaultAttributePresenter(),
    override val facetString: (String, String, Boolean) -> String = { _, value, _ ->
        value
    },
    override val facetBoolean: (String, Boolean, Boolean) -> String = { attribute, _, _ ->
        attributePresenter(attribute)
    },
    override val facetNumber: (String, Number, Boolean) -> String = { attribute, value, _ ->
        "${attributePresenter(attribute)}: $value"
    },
    override val tag: (String, String, Boolean) -> String = { _, value, _ -> value },
    override val numericComparison: (String, NumericOperator, Number, Boolean) -> String = { attribute, operator, value, _ ->
        "${attributePresenter(attribute)} ${operator.raw} $value"
    },
    override val numericRange: (String, Number, Number, Boolean) -> String = { attribute, lowerBound, upperBound, _ ->
        "${attributePresenter(attribute)}: $lowerBound to $upperBound"
    },
) : FilterPresenter

@Deprecated(message = "use DefaultFilterPresenter instead", replaceWith = ReplaceWith("DefaultFilterPresenter"))
public typealias FilterPresenterImpl = DefaultFilterPresenter
