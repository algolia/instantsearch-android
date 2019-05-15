package com.algolia.instantsearch.helper.filter.list

import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.helper.filter.state.*
import com.algolia.search.model.filter.Filter


public fun FilterListViewModel.Facet.connectFilterState(
    filterState: FilterState,
    groupID: FilterGroupID = FilterGroupID.Or()
) {
    connect(filterState, groupID) { getFacetFilters(groupID) }
}

public fun FilterListViewModel.Numeric.connectFilterState(
    filterState: FilterState,
    groupID: FilterGroupID = FilterGroupID.And()
) {
    connect(filterState, groupID) { getNumericFilters(groupID) }
}


public fun FilterListViewModel.Tag.connectFilterState(
    filterState: FilterState,
    groupID: FilterGroupID = FilterGroupID.And()
) {
    connect(filterState, groupID) { getTagFilters(groupID) }
}

public fun FilterListViewModel.All.connectFilterState(
    filterState: FilterState,
    groupID: FilterGroupID = FilterGroupID.And()
) {
    connect(filterState, groupID) { getFilters(groupID) }
}

internal inline fun <reified T : Filter> FilterListViewModel<T>.connect(
    filterState: FilterState,
    groupID: FilterGroupID,
    crossinline getSelections: Filters.() -> Set<T>
) {
    onSelectionsComputed += { selections ->
        filterState.notify {
            when (selectionMode) {
                SelectionMode.Single -> clear(groupID)
                SelectionMode.Multiple -> remove(groupID, items.toSet())
            }
            add(groupID, selections)
        }
    }
    val onChange: (Filters) -> Unit = { filters ->
        selections = filters.getSelections()
    }

    onChange(filterState)
    filterState.onChange += onChange
}