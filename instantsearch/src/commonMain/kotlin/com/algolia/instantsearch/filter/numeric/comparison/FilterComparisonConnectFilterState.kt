package com.algolia.instantsearch.filter.numeric.comparison

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.number.NumberPresenter
import com.algolia.instantsearch.core.number.DefaultNumberPresenter
import com.algolia.instantsearch.core.number.NumberView
import com.algolia.instantsearch.core.number.NumberViewModel
import com.algolia.instantsearch.core.number.connectView
import com.algolia.instantsearch.filter.numeric.comparison.internal.FilterComparisonConnectionFilterState
import com.algolia.instantsearch.filter.state.FilterGroupID
import com.algolia.instantsearch.filter.state.FilterOperator
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.migration2to3.Attribute
import com.algolia.instantsearch.migration2to3.NumericOperator

public fun <T> NumberViewModel<T>.connectFilterState(
    filterState: FilterState,
    attribute: Attribute,
    operator: NumericOperator,
    groupID: FilterGroupID = FilterGroupID(attribute, FilterOperator.And),
): Connection where T : Number, T : Comparable<T> {
    return FilterComparisonConnectionFilterState(this, filterState, attribute, operator, groupID)
}

/**
 * Create a connection between a view and the filter numeric comparison components.
 *
 * @param view the view that renders the numeric value
 * @param presenter defines the way we want to display the numeric value
 */
public fun <T> FilterComparisonConnector<T>.connectView(
    view: NumberView<T>,
    presenter: NumberPresenter<T> = DefaultNumberPresenter,
): Connection where T : Number, T : Comparable<T> {
    return viewModel.connectView(view, presenter)
}
