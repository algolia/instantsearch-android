package com.algolia.instantsearch.helper.filter.segment

import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.Filters


public fun FilterSegmentViewModel.connectFilterState(
    filterState: FilterState,
    groupID: FilterGroupID = FilterGroupID(FilterOperator.And)
) {
    val onChanged: (Filters) -> Unit = { filters ->
        selected = item.entries.find { it.value == filters.getFilters(groupID).firstOrNull() }?.key
    }

    onChanged(filterState)
    filterState.onChanged += onChanged
    onSelectedComputed += { computed ->
        filterState.notify {
            item[selected]?.let { remove(groupID, it) }
            if (selected != computed) {
                item[computed]?.let { add(groupID, it) }
            }
        }
    }
}