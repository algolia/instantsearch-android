package com.algolia.instantsearch.helper.filter.current

import com.algolia.instantsearch.core.event.EventViewModel
import com.algolia.instantsearch.core.event.EventViewModelImpl
import com.algolia.instantsearch.core.map.MapViewModel
import com.algolia.search.model.filter.Filter


class CurrentFiltersViewModel(
    items: Map<String, Filter> = mapOf()
) : MapViewModel<String, Filter>(items),
    EventViewModel<String> by EventViewModelImpl() {

    fun getFilters(): Set<Filter> = item.values.toSet()

    fun clearFilter(filter: Filter) {
        clearFilter(item.entries.firstOrNull { it.value == filter }?.key)
    }

    fun clearFilter(identifier: String?) {
        identifier?.let {
            onTriggered.forEach { it(identifier) }
            item = item.toMutableMap().apply { remove(identifier) }
        }
    }
}