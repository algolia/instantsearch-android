package filter

import com.algolia.search.model.filter.Filter


public interface Filters {

    fun getFacetFilters(groupID: FilterGroupID): Set<Filter.Facet>?

    fun getTagFilters(groupID: FilterGroupID): Set<Filter.Tag>?

    fun getNumericFilters(groupID: FilterGroupID): Set<Filter.Numeric>?

    fun getFacetGroups(): Map<FilterGroupID, Set<Filter.Facet>>

    fun getTagGroups(): Map<FilterGroupID, Set<Filter.Tag>>

    fun getNumericGroups(): Map<FilterGroupID, Set<Filter.Numeric>>

    fun getFilters(groupID: FilterGroupID): Set<Filter>

    fun getFilters(): Set<Filter>

    fun <T : Filter> contains(groupID: FilterGroupID, filter: T): Boolean
}