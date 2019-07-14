package com.algolia.instantsearch.helper.filter.segment

import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState


public fun FilterSegmentViewModel.connectFilterState(
    filterState: FilterState,
    groupID: FilterGroupID = FilterGroupID(FilterOperator.And)
) {
    filterState.filters.subscribePast { filters ->
        selected = item.entries.find { it.value == filters.getFilters(groupID).firstOrNull() }?.key
    }
    onSelectedComputed += { computed ->
        filterState.notify {
            item[selected]?.let { remove(groupID, it) }
            if (selected != computed) {
                item[computed]?.let { add(groupID, it) }
            }
        }
    }
}