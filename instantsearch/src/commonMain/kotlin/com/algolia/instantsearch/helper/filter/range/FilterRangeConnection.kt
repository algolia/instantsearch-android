package com.algolia.instantsearch.helper.filter.range

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.number.range.NumberRangeView
import com.algolia.instantsearch.core.number.range.connectView
import com.algolia.instantsearch.helper.filter.range.internal.FilterRangeConnectionFilterState
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherQuery
import com.algolia.search.model.Attribute
import com.algolia.search.model.response.ResponseSearch

public fun <T> FilterRangeViewModel<T>.connectFilterState(
    filterState: FilterState,
    attribute: Attribute,
    groupID: FilterGroupID = FilterGroupID(attribute, FilterOperator.And),
): Connection where T : Number, T : Comparable<T> {
    return FilterRangeConnectionFilterState(this, filterState, attribute, groupID)
}

/**
 * Create a connection between a view and the filter range components
 *
 * @param view the view that will render the numeric range filter
 */
public fun <T> FilterRangeConnector<T>.connectView(
    view: NumberRangeView<T>,
): Connection where T : Number, T : Comparable<T> {
    return viewModel.connectView(view)
}

/**
 * Create a connection between a searcher and the filter range components for dynamic behavior.
 *
 * @param searcher searcher for facets and results extraction operations.
 * @param attribute attribute to dynamically use its facet stats results.
 */
public inline fun <reified T> FilterRangeConnector<T>.connectSearcher(
    searcher: SearcherQuery<*, ResponseSearch>,
    attribute: Attribute,
): Connection where T : Number, T : Comparable<T> {
    return viewModel.connectSearcher(searcher, attribute)
}

/**
 * Create a connection between a searcher and the filter range components for dynamic behavior.
 *
 * @param searcher searcher for facets and results extraction operations.
 * @param attribute attribute to dynamically use its facet stats results.
 */
public inline fun <reified T> FilterRangeViewModel<T>.connectSearcher(
    searcher: SearcherQuery<*, ResponseSearch>,
    attribute: Attribute,
): Connection where T : Number, T : Comparable<T> {
    return FilterRangeConnectionSearcher(this, searcher, attribute, T::class)
}

/**
 * Create a connection between a searcher and the filter range components for dynamic behavior.
 *
 * @param searcher searcher for facets and results extraction operations.
 * @param attribute attribute to dynamically use its facet stats results.
 * @param mapper explicit mapper to transform facets stats min/max results to the view model's bounds.
 */
public inline fun <reified T> FilterRangeConnector<T>.connectSearcher(
    searcher: SearcherQuery<*, ResponseSearch>,
    attribute: Attribute,
    noinline mapper: (Number) -> T,
): Connection where T : Number, T : Comparable<T> {
    return viewModel.connectSearcher(searcher, attribute, mapper)
}

/**
 * Create a connection between a searcher and the filter range components for dynamic behavior.
 *
 * @param searcher searcher for facets and results extraction operations.
 * @param attribute attribute to dynamically use its facet stats results.
 * @param mapper explicit mapper to transform facets stats min/max results to the view model's bounds.
 */
public inline fun <reified T> FilterRangeViewModel<T>.connectSearcher(
    searcher: SearcherQuery<*, ResponseSearch>,
    attribute: Attribute,
    noinline mapper: (Number) -> T,
): Connection where T : Number, T : Comparable<T> {
    return FilterRangeConnectionSearcher(this, searcher, attribute, mapper)
}
