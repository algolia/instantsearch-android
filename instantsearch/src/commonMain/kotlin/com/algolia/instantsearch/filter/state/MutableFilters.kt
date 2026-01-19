package com.algolia.instantsearch.filter.state


import com.algolia.instantsearch.filter.Filter
import com.algolia.instantsearch.hierarchical.HierarchicalFilter

public interface MutableFilters : Filters {

    public fun <T : Filter> add(groupID: FilterGroupID, vararg filters: T)

    public fun <T : Filter> remove(groupID: FilterGroupID, vararg filters: T)

    public fun <T : Filter> toggle(groupID: FilterGroupID, filter: T)

    public fun add(attribute: String, hierarchicalFilter: HierarchicalFilter)

    public fun remove(attribute: String)

    public fun set(map: Map<FilterGroupID, Set<Filter>>)

    public fun clear(vararg groupIDs: FilterGroupID)

    public fun clearExcept(groupIDs: List<FilterGroupID>)
}
