package com.algolia.instantsearch.helper.filter.list.internal

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.helper.filter.list.FilterListViewModel
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.Filters
import com.algolia.search.model.filter.Filter

internal inline fun <reified T : Filter> FilterListViewModel<T>.connect(
    filterState: FilterState,
    groupID: FilterGroupID,
    noinline getSelections: Filters.() -> Set<T>,
): Connection {
    return FilterListConnectionFilterState(this, filterState, groupID, getSelections)
}
