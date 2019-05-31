package com.algolia.instantsearch.helper.filter.clear

import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState


fun FilterClearViewModel.connectFilterState(
    filterState: FilterState,
    vararg groupID: FilterGroupID
) {
    connectFilterState(filterState, false, *groupID)
}

fun FilterClearViewModel.connectFilterState(
    filterState: FilterState,
    groupIDs: List<FilterGroupID>,
    exceptGroup: Boolean = false
) {
    connectFilterState(filterState, exceptGroup, *groupIDs.toTypedArray())
}


fun FilterClearViewModel.connectFilterState(
    filterState: FilterState,
    exceptGroup: Boolean,
    vararg groupID: FilterGroupID
) {
    onClicked += { filterState.notify { if (exceptGroup) clearExcept(groupID.asList()) else clear(*groupID) } }
}