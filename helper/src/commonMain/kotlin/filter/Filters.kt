package filter

import com.algolia.search.model.filter.Filter
import search.GroupID


public interface Filters {

    fun getFacets(groupID: GroupID): Set<Filter.Facet>?

    fun getTags(groupID: GroupID): Set<Filter.Tag>?

    fun getNumerics(groupID: GroupID): Set<Filter.Numeric>?

    fun getFacets(): Map<GroupID, Set<Filter.Facet>>

    fun getTags(): Map<GroupID, Set<Filter.Tag>>

    fun getNumerics(): Map<GroupID, Set<Filter.Numeric>>

    fun <T : Filter> contains(groupID: GroupID, filter: T): Boolean
}