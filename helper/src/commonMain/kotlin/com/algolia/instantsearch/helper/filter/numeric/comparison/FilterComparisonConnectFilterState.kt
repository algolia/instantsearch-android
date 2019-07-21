package com.algolia.instantsearch.helper.filter.numeric.comparison

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.autoConnect
import com.algolia.instantsearch.core.number.NumberViewModel
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.NumericOperator


public fun <T> NumberViewModel<T>.connectFilterState(
    filterState: FilterState,
    attribute: Attribute,
    operator: NumericOperator,
    groupID: FilterGroupID = FilterGroupID(attribute, FilterOperator.And),
    connect: Boolean = true
): Connection where T : Number, T : Comparable<T> {
    return FilterComparisonConnectionFilterState(this, filterState, attribute, operator, groupID).autoConnect(connect)
}