@file:JvmName("FilterRange")

package com.algolia.instantsearch.helper.filter.range

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.number.range.NumberRangeView
import com.algolia.instantsearch.core.number.range.connectView
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.Attribute
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads

/**
 * Connects this FilterRangeViewModel to a FilterState, updating its filters when the range changes
 * and updating the viewModel's data when the filterState changes.
 */
@JvmOverloads
public fun <T> FilterRangeViewModel<T>.connectFilterState(
    filterState: FilterState,
    attribute: Attribute,
    groupID: FilterGroupID = FilterGroupID(attribute, FilterOperator.And)
): Connection where T : Number, T : Comparable<T> {
    return FilterRangeConnectionFilterState(this, filterState, attribute, groupID)
}

public fun <T> FilterRangeConnector<T>.connectView(
    view: NumberRangeView<T>
): Connection where T : Number, T : Comparable<T> {
    return viewModel.connectView(view)
}