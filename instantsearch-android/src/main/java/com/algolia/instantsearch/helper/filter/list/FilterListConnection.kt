package com.algolia.instantsearch.helper.filter.list

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.selectable.list.connectView
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.Filters
import com.algolia.search.model.filter.Filter

public fun FilterListViewModel.Facet.connectFilterState(
    filterState: FilterState,
    groupID: FilterGroupID = FilterGroupID(FilterOperator.Or),
): Connection {
    return connect(filterState, groupID) { getFacetFilters(groupID) }
}

public fun FilterListViewModel.Numeric.connectFilterState(
    filterState: FilterState,
    groupID: FilterGroupID = FilterGroupID(FilterOperator.And),
): Connection {
    return connect(filterState, groupID) { getNumericFilters(groupID) }
}

public fun FilterListViewModel.Tag.connectFilterState(
    filterState: FilterState,
    groupID: FilterGroupID = FilterGroupID(FilterOperator.And),
): Connection {
    return connect(filterState, groupID) { getTagFilters(groupID) }
}

public fun FilterListViewModel.All.connectFilterState(
    filterState: FilterState,
    groupID: FilterGroupID = FilterGroupID(FilterOperator.And),
): Connection {
    return connect(filterState, groupID) { getFilters(groupID) }
}

internal inline fun <reified T : Filter> FilterListViewModel<T>.connect(
    filterState: FilterState,
    groupID: FilterGroupID,
    noinline getSelections: Filters.() -> Set<T>,
): Connection {
    return FilterListConnectionFilterState(this, filterState, groupID, getSelections)
}

public fun <T : Filter> FilterListConnector<T>.connectView(
    view: FilterListView<T>,
): Connection {
    return viewModel.connectView(view)
}
