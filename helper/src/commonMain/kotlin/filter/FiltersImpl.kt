package filter

import com.algolia.search.model.filter.Filter
import search.GroupID


internal class FiltersImpl(
    private val facets: Map<GroupID, Set<Filter.Facet>>,
    private val tags: Map<GroupID, Set<Filter.Tag>>,
    private val numerics: Map<GroupID, Set<Filter.Numeric>>
) : Filters {

    override fun getFacets(groupID: GroupID): Set<Filter.Facet>? {
        return facets[groupID]
    }

    override fun getTags(groupID: GroupID): Set<Filter.Tag>? {
        return tags[groupID]
    }

    override fun getNumerics(groupID: GroupID): Set<Filter.Numeric>? {
        return numerics[groupID]
    }

    override fun getFacets(): Map<GroupID, Set<Filter.Facet>> {
        return facets
    }

    override fun getTags(): Map<GroupID, Set<Filter.Tag>> {
        return tags
    }

    override fun getNumerics(): Map<GroupID, Set<Filter.Numeric>> {
        return numerics
    }

    override fun <T : Filter> contains(groupID: GroupID, filter: T): Boolean {
        return when (filter) {
            is Filter.Facet -> facets[groupID]?.contains(filter)
            is Filter.Tag -> tags[groupID]?.contains(filter)
            is Filter.Numeric -> numerics[groupID]?.contains(filter)
            else -> null
        } ?: false
    }
}