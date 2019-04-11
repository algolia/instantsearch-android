package search

import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.FilterGroup


typealias FilterState = Map<Group<*>, Set<Filter>>

internal fun SearchFilterState.toFilterGroups(): List<FilterGroup<*>> {
    return get().map { (group, filters) ->
        when (group) {
            is Group.And -> FilterGroup.And(filters)
            is Group.Or -> FilterGroup.Or(filters)
        }
    }
}

@Suppress("UNCHECKED_CAST")
fun <T : Filter> FilterState.getFilters(group: Group<T>): Set<T>? {
    return this[group] as? Set<T>
}