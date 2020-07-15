package com.algolia.instantsearch.helper.filter.range

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.number.range.NumberRangeView
import com.algolia.instantsearch.core.number.range.connectView
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.Attribute

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

public inline fun <reified T> FilterRangeConnector<T>.connectSearcher(
    searcherSingleIndex: SearcherSingleIndex,
    attribute: Attribute
): Connection where T : Number, T : Comparable<T> {
    return viewModel.connectSearcher(searcherSingleIndex, attribute)
}

public inline fun <reified T> FilterRangeConnector<T>.connectSearcher(
    searcherSingleIndex: SearcherSingleIndex,
    attribute: Attribute,
    mapper: (Number) -> T
): Connection where T : Number, T : Comparable<T> {
    return viewModel.connectSearcher(searcherSingleIndex, attribute, mapper)
}

public inline fun <reified T> FilterRangeViewModel<T>.connectSearcher(
    searcherSingleIndex: SearcherSingleIndex,
    attribute: Attribute
): Connection where T : Number, T : Comparable<T> {
    return FilterRangeConnectionSearcher(this, searcherSingleIndex, attribute, T::class)
}

public inline fun <reified T> FilterRangeViewModel<T>.connectSearcher(
    searcherSingleIndex: SearcherSingleIndex,
    attribute: Attribute,
    mapper: (Number) -> T
): Connection where T : Number, T : Comparable<T> {
    return FilterRangeConnectionSearcher(this, searcherSingleIndex, attribute, T::class)
}
