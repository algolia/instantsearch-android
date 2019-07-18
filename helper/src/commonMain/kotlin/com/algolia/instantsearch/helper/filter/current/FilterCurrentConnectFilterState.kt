package com.algolia.instantsearch.helper.filter.current

import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.filter.Filter

internal fun Map<FilterGroupID, Set<Filter>>.toFilterAndIds(): Map<FilterAndID, Filter> {
    return flatMap { (key, value) -> value.map { FilterAndID(key, it) to it } }.toMap()
}

public fun FilterCurrentViewModel.connectFilterState(
    filterState: FilterState,
    groupID: FilterGroupID? = null
) {
    filterState.filters.subscribePast { filters ->
        val groups = filters.getGroups().filter { groupID == null || it.key == groupID }
        val filterAndIDs = groups.toFilterAndIds()

        map.set(filterAndIDs)
    }
    event.subscribe {
        filterState.notify {
            if (groupID != null) {
                clear(groupID)
            } else {
                clear()
            }
            it.get().forEach { add(it.key.first, it.value) }
        }
    }
}