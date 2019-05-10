package filter

import com.algolia.search.model.filter.Filter


internal data class FiltersImpl(
    private val facets: Map<FilterGroupID, Set<Filter.Facet>>,
    private val tags: Map<FilterGroupID, Set<Filter.Tag>>,
    private val numerics: Map<FilterGroupID, Set<Filter.Numeric>>
) : Filters {

    override fun getFacetFilters(groupID: FilterGroupID): Set<Filter.Facet>? {
        return facets[groupID]
    }

    override fun getTagFilters(groupID: FilterGroupID): Set<Filter.Tag>? {
        return tags[groupID]
    }

    override fun getNumericFilters(groupID: FilterGroupID): Set<Filter.Numeric>? {
        return numerics[groupID]
    }

    override fun getFacetGroups(): Map<FilterGroupID, Set<Filter.Facet>> {
        return facets
    }

    override fun getTagGroups(): Map<FilterGroupID, Set<Filter.Tag>> {
        return tags
    }

    override fun getNumericGroups(): Map<FilterGroupID, Set<Filter.Numeric>> {
        return numerics
    }

    override fun getFilters(groupID: FilterGroupID): Set<Filter> {
        return mutableSetOf<Filter>().apply {
            getFacetFilters(groupID)?.let { this += it }
            getTagFilters(groupID)?.let { this += it }
            getNumericFilters(groupID)?.let { this += it }
        }
    }

    override fun <T : Filter> contains(groupID: FilterGroupID, filter: T): Boolean {
        return when (filter) {
            is Filter.Facet -> facets[groupID]?.contains(filter)
            is Filter.Tag -> tags[groupID]?.contains(filter)
            is Filter.Numeric -> numerics[groupID]?.contains(filter)
            else -> null
        } ?: false
    }

    override fun toString(): String {
        return "FiltersImpl(facets=$facets, tags=$tags, numerics=$numerics)"
    }
}