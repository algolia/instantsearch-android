package com.algolia.instantsearch.helper.filter.range

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.number.range.NumberRangeView
import com.algolia.instantsearch.core.number.range.connectionView
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.Attribute


public fun <T> FilterRangeViewModel<T>.connectionFilterState(
    filterState: FilterState,
    attribute: Attribute,
    groupID: FilterGroupID
): Connection where T : Number, T : Comparable<T> {
    return FilterRangeConnectionFilterState(this, filterState, attribute, groupID)
}

public fun <T> FilterRangeWidget<T>.connectionView(
    view: NumberRangeView<T>
): Connection where T : Number, T : Comparable<T> {
    return viewModel.connectionView(view)
}