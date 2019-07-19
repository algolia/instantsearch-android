package com.algolia.instantsearch.helper.filter.toggle

import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.filter.Filter


public fun FilterToggleViewModel.connectFilterState(
    filterState: FilterState,
    default: Filter? = null,
    groupID: FilterGroupID = FilterGroupID(item.value.attribute, FilterOperator.And)
) {
    filterState.filters.subscribePast { filters ->
        isSelected.value = filters.contains(groupID, item.value)
    }
    if (default != null) filterState.add(groupID, default)
    eventSelection.subscribe { isSelected ->
        filterState.notify {
            if (isSelected) {
                if (default != null) remove(groupID, default)
                add(groupID, item.value)
            } else {
                remove(groupID, item.value)
                if (default != null) add(groupID, default)
            }
        }
    }
}