package filter

import com.algolia.search.model.filter.Filter
import search.GroupID


data class FilterState(
    var facet: Map<GroupID, Set<Filter.Facet>> = mapOf(),
    var tag: Map<GroupID, Set<Filter.Tag>> = mapOf(),
    var numeric: Map<GroupID, Set<Filter.Numeric>> = mapOf()
)