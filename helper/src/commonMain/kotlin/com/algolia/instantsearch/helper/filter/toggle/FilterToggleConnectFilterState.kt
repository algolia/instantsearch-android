package com.algolia.instantsearch.helper.filter.toggle

import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.Filters
import com.algolia.search.model.filter.Filter


public fun FilterToggleViewModel.connectFilterState(
    filterState: FilterState,
    default: Filter? = null,
    groupID: FilterGroupID = FilterGroupID.And(item.attribute)
) {
    if (default != null) filterState.add(groupID, default)
    onIsSelectedComputed += { isSelected ->
        filterState.notify {
            if (isSelected) {
                if (default != null) remove(groupID, default)
                add(groupID, item)
            } else {
                remove(groupID, item)
                if (default != null) add(groupID, default)
            }
        }
    }
    val onChanged: (Filters) -> Unit = { filters ->
        isSelected = filters.contains(groupID, item)
    }

    onChanged(filterState)
    filterState.onChanged += onChanged
}