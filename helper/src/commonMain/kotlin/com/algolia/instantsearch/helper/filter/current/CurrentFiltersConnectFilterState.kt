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
    item = filtersToItem(filterState)
    println("connected with ${item.size}  items=$item")
    val onTriggered: (String) -> Unit = { identifier ->
        println("onTriggered($identifier): ${item[identifier]}")
        item[identifier]?.let {
            filterState.notify {
                val groupID = groupFromIdentifier(identifier)
                remove(groupID, it)
                println("removing $groupID: $it")
            }
        }
    }
    val onChanged: (Filters) -> Unit = {
        item = filtersToItem(it)
        println("onChanged($it) -> setting item")
    }

    this.onTriggered += onTriggered
    filterState.onChanged += onChanged
}

private fun filtersToItem(filters: Filters): Map<String, Filter> {
    val groups = filters.getGroups()
    val map = mutableMapOf<String, Filter>().apply {
        groups.entries.forEach { group ->
            group.value.forEach {
                put(filterIdentifier(group.key, it), it)
            }
        }
    }
    return map
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
