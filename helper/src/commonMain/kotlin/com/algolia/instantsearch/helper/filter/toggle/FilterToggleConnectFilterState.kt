package com.algolia.instantsearch.helper.filter.toggle

import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.Filters


public fun FilterToggleViewModel.connectFilterState(
    filterState: FilterState,
    groupID: FilterGroupID = FilterGroupID.Or(item.attribute)
) {
    onIsSelectedComputed += { isSelected ->
        filterState.notify {
            if (isSelected) add(groupID, item) else remove(groupID, item)
        }
    }
    val onChange: (Filters) -> Unit = { filters ->
        isSelected = filters.contains(groupID, item)
    }

    onChange(filterState)
    filterState.onChange += onChange
}