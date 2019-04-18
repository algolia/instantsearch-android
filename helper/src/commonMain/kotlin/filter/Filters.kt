package filter

import com.algolia.search.model.filter.Filter


public interface Filters {

    fun getFacets(groupID: FilterGroupID): Set<Filter.Facet>?

    fun getTags(groupID: FilterGroupID): Set<Filter.Tag>?

    fun getNumerics(groupID: FilterGroupID): Set<Filter.Numeric>?

    fun getFacets(): Map<FilterGroupID, Set<Filter.Facet>>

    fun getTags(): Map<FilterGroupID, Set<Filter.Tag>>

    fun getNumerics(): Map<FilterGroupID, Set<Filter.Numeric>>

    fun <T : Filter> contains(groupID: FilterGroupID, filter: T): Boolean
}