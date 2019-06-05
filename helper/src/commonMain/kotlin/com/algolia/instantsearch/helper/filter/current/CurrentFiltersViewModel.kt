package com.algolia.instantsearch.helper.filter.current

import com.algolia.instantsearch.core.map.MapViewModel
import com.algolia.search.model.filter.Filter


class CurrentFiltersViewModel(
    items: Map<String, Filter> = mapOf(),
    val presenter: (Map<String, Filter>) -> Map<String, Filter> = { it }
) : MapViewModel<String, Filter>(items) {

    fun getFilters() = item.values

    fun clearFilter(filter: Filter) {
        clearFilter(item.entries.firstOrNull { it.value == filter }?.key)
    }

    fun clearFilter(identifier: String?) {
        identifier?.let {
            item = presenter(item.toMutableMap().apply { remove(identifier) })
            onTriggered.forEach { it(identifier) }
        }
    }
}