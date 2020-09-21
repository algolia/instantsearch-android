package com.algolia.instantsearch.helper.filter.state

import com.algolia.search.model.Attribute

public data class FilterGroupID(
    val name: String = "",
    val operator: FilterOperator = FilterOperator.And,
) {

    public constructor(operator: FilterOperator) : this("", operator = operator)

    public constructor(
        attribute: Attribute,
        operator: FilterOperator = FilterOperator.And,
    ) : this(attribute.raw, operator)
}
