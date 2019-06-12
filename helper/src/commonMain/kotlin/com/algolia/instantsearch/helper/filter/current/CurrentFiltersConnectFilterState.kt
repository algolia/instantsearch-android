package com.algolia.instantsearch.helper.filter.current

import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.Filters
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter


public fun CurrentFiltersViewModel.connectFilterState(
    filterState: FilterState
    //TODO added feature: groupID parameter to only see its filters
) {
    val onChanged: (Filters) -> Unit = {
        item = filtersToItem(it)
    }

    onChanged(filterState.filters)
    filterState.onChanged += onChanged
    onMapComputed += { map ->
        filterState.notify {
            val obsoleteFilters = item.entries.filter { !map.containsKey(it.key) }
            val newFilters = item.filter { it !in obsoleteFilters }

            obsoleteFilters.forEach { remove(groupFromIdentifier(it.key), it.value) }
            newFilters.forEach { add(groupFromIdentifier(it.key), it.value) }
        }
    }
}

private fun filtersToItem(filters: Filters): Map<String, Filter> {
    return filters.getGroups().entries.flatMap { entry ->
        entry.value.map { Pair(filterIdentifier(entry.key, it), it) }
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

        return if (!it[0].isNullOrEmpty()) {
            FilterGroupID(Attribute(it[0]), operator)
        } else FilterGroupID("", operator)
    }
}
