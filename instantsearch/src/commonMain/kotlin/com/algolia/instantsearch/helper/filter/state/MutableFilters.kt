package com.algolia.instantsearch.helper.filter.state

import com.algolia.instantsearch.helper.hierarchical.HierarchicalFilter
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter


public interface MutableFilters : Filters {

    public fun <T : Filter> add(groupID: FilterGroupID, vararg filters: T)

    public fun <T : Filter> remove(groupID: FilterGroupID, vararg filters: T)

    public fun <T : Filter> toggle(groupID: FilterGroupID, filter: T)

    public fun add(attribute: Attribute, hierarchicalFilter: HierarchicalFilter)

    public fun remove(attribute: Attribute)

    public fun set(map: Map<FilterGroupID, Set<Filter>>)

    public fun clear(vararg groupIDs: FilterGroupID)

    public fun clearExcept(groupIDs: List<FilterGroupID>)
}