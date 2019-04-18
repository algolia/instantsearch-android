package filter

import com.algolia.search.model.filter.Filter


internal class FiltersImpl(
    private val facets: Map<FilterGroupID, Set<Filter.Facet>>,
    private val tags: Map<FilterGroupID, Set<Filter.Tag>>,
    private val numerics: Map<FilterGroupID, Set<Filter.Numeric>>
) : Filters {

    override fun getFacets(groupID: FilterGroupID): Set<Filter.Facet>? {
        return facets[groupID]
    }

    override fun getTags(groupID: FilterGroupID): Set<Filter.Tag>? {
        return tags[groupID]
    }

    override fun getNumerics(groupID: FilterGroupID): Set<Filter.Numeric>? {
        return numerics[groupID]
    }

    override fun getFacets(): Map<FilterGroupID, Set<Filter.Facet>> {
        return facets
    }

    override fun getTags(): Map<FilterGroupID, Set<Filter.Tag>> {
        return tags
    }

    override fun getNumerics(): Map<FilterGroupID, Set<Filter.Numeric>> {
        return numerics
    }

    override fun <T : Filter> contains(groupID: FilterGroupID, filter: T): Boolean {
        return when (filter) {
            is Filter.Facet -> facets[groupID]?.contains(filter)
            is Filter.Tag -> tags[groupID]?.contains(filter)
            is Filter.Numeric -> numerics[groupID]?.contains(filter)
            else -> null
        } ?: false
    }
}