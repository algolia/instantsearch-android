package com.algolia.instantsearch.helper.filter.current

import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.Filters
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter


public fun CurrentFiltersViewModel.connectFilterState(
    filterState: FilterState,
    groupID: FilterGroupID? = null
) {
    val onChanged: (Filters) -> Unit = {
        map.set(filtersToItem(it, groupID))
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
            it.get().forEach { add(groupFromIdentifier(it.key), it.value) }
        }
    }
}

private fun filtersToItem(
    filters: Filters,
    groupID: FilterGroupID? = null
): Map<String, Filter> {
    return filters.getGroups()
        .filter { groupID == null || it.key == groupID }
        .flatMap { (key, value) ->
            value.map { filterIdentifier(key, it) to it }
    }.toMap()
}

fun filterIdentifier(groupID: FilterGroupID, filter: Filter): String {
    return "${groupID.name}%${groupID.operator}%$filter"
}

fun groupFromIdentifier(identifier: String): FilterGroupID {
    identifier.split("%").also {
        val operator = when (it[1]) {
            "Or" -> FilterOperator.Or
            else -> FilterOperator.And
        }

        return if (it[0].isNotEmpty()) {
            FilterGroupID(Attribute(it[0]), operator)
        } else FilterGroupID("", operator)
    }
}
