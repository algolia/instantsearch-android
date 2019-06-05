package com.algolia.instantsearch.helper.filter.state

import com.algolia.search.model.filter.Filter


public interface Filters {

    public fun getFacetFilters(groupID: FilterGroupID): Set<Filter.Facet>

    public fun getTagFilters(groupID: FilterGroupID): Set<Filter.Tag>

    public fun getNumericFilters(groupID: FilterGroupID): Set<Filter.Numeric>

    public fun getFacetGroups(): Map<FilterGroupID, Set<Filter.Facet>>

    public fun getTagGroups(): Map<FilterGroupID, Set<Filter.Tag>>

    public fun getNumericGroups(): Map<FilterGroupID, Set<Filter.Numeric>>

    public fun getGroups(): Map<FilterGroupID, Set<Filter>>

    public fun getFilters(groupID: FilterGroupID): Set<Filter>

    public fun getFilters(): Set<Filter>

    public fun <T : Filter> contains(groupID: FilterGroupID, filter: T): Boolean
}