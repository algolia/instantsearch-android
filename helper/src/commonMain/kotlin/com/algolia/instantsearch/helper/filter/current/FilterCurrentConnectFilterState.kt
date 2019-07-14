package com.algolia.instantsearch.helper.filter.current

import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.Filters
import com.algolia.search.model.filter.Filter

internal fun Map<FilterGroupID, Set<Filter>>.toFilterAndIds(): Set<FilterAndID> {
    return flatMap { (key, value) -> value.map { key to it } }.toSet()
}

public fun FilterCurrentViewModel.connectFilterState(
    filterState: FilterState,
    groupID: FilterGroupID? = null
) {
    val onChanged: (Filters) -> Unit = { filters ->
        val groups = filters.getGroups().filter { groupID == null || it.key == groupID }
        val filterAndIDs = groups.toFilterAndIds()

        this.filters.set(filterAndIDs)
    }

    onChanged(filterState.filters)
    filterState.onChanged += onChanged
    event.subscribe {
        filterState.notify {
            if (groupID != null) {
                clear(groupID)
            } else {
                clear()
            }
            it.forEach { add(it.first, it.second) }
        }
    }
}