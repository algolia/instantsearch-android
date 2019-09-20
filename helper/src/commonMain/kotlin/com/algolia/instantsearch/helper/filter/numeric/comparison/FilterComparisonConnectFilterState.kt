@file:JvmName("FilterComparison")

package com.algolia.instantsearch.helper.filter.numeric.comparison

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.number.NumberViewModel
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.NumericOperator
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads

/**
 * Connects this NumberViewModel to a FilterState, updating its filters when the number changes
 * and updating the viewModel's data when the filterState changes.
 */
@JvmOverloads
public fun <T> NumberViewModel<T>.connectFilterState(
    filterState: FilterState,
    attribute: Attribute,
    operator: NumericOperator,
    groupID: FilterGroupID = FilterGroupID(attribute, FilterOperator.And)
): Connection where T : Number, T : Comparable<T> {
    return FilterComparisonConnectionFilterState(this, filterState, attribute, operator, groupID)
}