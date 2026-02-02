package com.algolia.instantsearch.filter.state

import com.algolia.instantsearch.filter.Filter
import com.algolia.instantsearch.hierarchical.HierarchicalFilter

public interface Filters {

    public fun getFacetFilters(groupID: FilterGroupID): Set<Filter.Facet>

    public fun getTagFilters(groupID: FilterGroupID): Set<Filter.Tag>

    public fun getNumericFilters(groupID: FilterGroupID): Set<Filter.Numeric>

    public fun getHierarchicalFilters(attribute: String): HierarchicalFilter?

    public fun getFacetGroups(): Map<FilterGroupID, Set<Filter.Facet>>

    public fun getTagGroups(): Map<FilterGroupID, Set<Filter.Tag>>

    public fun getNumericGroups(): Map<FilterGroupID, Set<Filter.Numeric>>

    public fun getHierarchicalGroups(): Map<String, HierarchicalFilter>

    public fun getGroups(): Map<FilterGroupID, Set<Filter>>

    public fun getFilters(groupID: FilterGroupID): Set<Filter>

    public fun getFilters(): Set<Filter>

    public fun <T : Filter> contains(groupID: FilterGroupID, filter: T): Boolean
}
