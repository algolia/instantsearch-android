package com.algolia.instantsearch.helper.filter.list

import com.algolia.instantsearch.core.observable.ObservableKey
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.helper.filter.state.*
import com.algolia.search.model.filter.Filter


public fun FilterListViewModel.Facet.connectFilterState(
    filterState: FilterState,
    groupID: FilterGroupID = FilterGroupID(FilterOperator.Or),
    key: ObservableKey? = null
) {
    connect(filterState, groupID, key) { getFacetFilters(groupID) }
}

public fun FilterListViewModel.Numeric.connectFilterState(
    filterState: FilterState,
    groupID: FilterGroupID = FilterGroupID(FilterOperator.And),
    key: ObservableKey? = null
) {
    connect(filterState, groupID, key) { getNumericFilters(groupID) }
}


public fun FilterListViewModel.Tag.connectFilterState(
    filterState: FilterState,
    groupID: FilterGroupID = FilterGroupID(FilterOperator.And),
    key: ObservableKey? = null
) {
    connect(filterState, groupID, key) { getTagFilters(groupID) }
}

public fun FilterListViewModel.All.connectFilterState(
    filterState: FilterState,
    groupID: FilterGroupID = FilterGroupID(FilterOperator.And),
    key: ObservableKey? = null
) {
    connect(filterState, groupID, key) { getFilters(groupID) }
}

internal inline fun <reified T : Filter> FilterListViewModel<T>.connect(
    filterState: FilterState,
    groupID: FilterGroupID,
    key: ObservableKey?,
    crossinline getSelections: Filters.() -> Set<T>
) {
    filterState.filters.subscribePast(key) { filters ->
        selections = filters.getSelections()
    }
    onSelectionsComputed += { selections ->
        filterState.notify {
            when (selectionMode) {
                SelectionMode.Single -> clear(groupID)
                SelectionMode.Multiple -> remove(groupID, item.toSet())
            }
            add(groupID, selections)
        }
    }
}