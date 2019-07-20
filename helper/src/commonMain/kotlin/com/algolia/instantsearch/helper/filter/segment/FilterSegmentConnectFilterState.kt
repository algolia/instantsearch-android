package com.algolia.instantsearch.helper.filter.segment

import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState


public fun FilterSegmentViewModel.connectFilterState(
    filterState: FilterState,
    groupID: FilterGroupID = FilterGroupID(FilterOperator.And)
) {
    filterState.filters.subscribePast { filters ->
        selected.value = map.value.entries.find { it.value == filters.getFilters(groupID).firstOrNull() }?.key
    }
    eventSelection.subscribe { computed ->
        filterState.notify {
            map.value[selected.value]?.let { remove(groupID, it) }
            if (selected.value != computed) {
                map.value[computed]?.let { add(groupID, it) }
            }
        }
    }
}