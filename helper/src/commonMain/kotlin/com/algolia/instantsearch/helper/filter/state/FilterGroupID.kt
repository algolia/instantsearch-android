package com.algolia.instantsearch.helper.filter.state

import com.algolia.search.model.Attribute
import kotlin.jvm.JvmOverloads

/**
 * An identifier for a group of filters and its operator.
 */
public data class FilterGroupID @JvmOverloads constructor(
    val name: String = "",
    val operator: FilterOperator = FilterOperator.And
) {

    public constructor(operator: FilterOperator) : this("", operator = operator)

    public constructor(
        attribute: Attribute,
        operator: FilterOperator = FilterOperator.And
    ) : this(attribute.raw, operator)
}