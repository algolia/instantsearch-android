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
    val groups = filters.getGroups()

    return mutableMapOf<String, Filter>().apply {
        groups.entries.forEach { group ->
            group.value.forEach {
                put(filterIdentifier(group.key, it), it)
            }
        }
    }
}

fun filterIdentifier(groupID: FilterGroupID, filter: Filter): String {
    return "${groupID.name}%${groupID.operator}%$filter"
}

fun groupFromIdentifier(identifier: String): FilterGroupID {
    identifier.split("%").also {
        val attribute = Attribute(it[0])
        val operator = when (it[1]) {
            "Or" -> FilterOperator.Or
            else -> FilterOperator.And
        }
        return FilterGroupID(attribute, operator)
    }
}
