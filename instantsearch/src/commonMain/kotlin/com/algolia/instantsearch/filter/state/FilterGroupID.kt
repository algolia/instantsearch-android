package com.algolia.instantsearch.filter.state


/**
 * Identifier of a group of filters.
 */
public data class FilterGroupID(
    val name: String = "",
    val operator: FilterOperator = FilterOperator.And,
) {

    public constructor(operator: FilterOperator) : this("", operator = operator)

}
