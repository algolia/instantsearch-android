package com.algolia.instantsearch.helper.filter.clear

import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState


public fun FilterClearViewModel.connectFilterState(
    filterState: FilterState,
    groupIDs: List<FilterGroupID> = listOf(),
    mode: ClearMode = ClearMode.Specified
) {
    onClicked += {
        filterState.notify {
            when (mode) {
                ClearMode.Specified -> clear(*groupIDs.toTypedArray())
                ClearMode.Except -> clearExcept(groupIDs)
            }
        }
    }
}