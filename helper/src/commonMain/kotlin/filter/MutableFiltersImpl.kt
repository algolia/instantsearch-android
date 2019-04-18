package filter

import com.algolia.search.model.filter.Filter


internal class MutableFiltersImpl(
    private val facets: MutableMap<FilterGroupID, Set<Filter.Facet>> = mutableMapOf(),
    private val tags: MutableMap<FilterGroupID, Set<Filter.Tag>> = mutableMapOf(),
    private val numerics: MutableMap<FilterGroupID, Set<Filter.Numeric>> = mutableMapOf()
) : MutableFilters, Filters by FiltersImpl(facets, tags, numerics) {

    private fun <T : Filter> Map<FilterGroupID, Set<T>>.getOrDefault(groupID: FilterGroupID): Set<T> {
        return getOrElse(groupID, { setOf() })
    }

    private fun <T : Filter> MutableMap<FilterGroupID, Set<T>>.modify(
        groupID: FilterGroupID,
        filter: T,
        operator: Set<T>.(T) -> Set<T>
    ) {
        val set = getOrDefault(groupID).operator(filter)

        if (set.isNotEmpty()) this[groupID] = set else remove(groupID)
    }

    private fun <T : Filter> MutableMap<FilterGroupID, Set<T>>.add(groupID: FilterGroupID, filter: T) {
        modify(groupID, filter, { plus(it) })
    }

    private fun <T : Filter> MutableMap<FilterGroupID, Set<T>>.remove(groupID: FilterGroupID, filter: T) {
        modify(groupID, filter, { minus(it) })
    }

    private fun <T : Filter> MutableMap<FilterGroupID, Set<T>>.clear(groupID: FilterGroupID) {
        remove(groupID)
    }

    override fun <T : Filter> add(groupID: FilterGroupID, vararg filters: T) {
        filters.forEach { filter ->
            when (filter) {
                is Filter.Facet -> facets.add(groupID, filter)
                is Filter.Tag -> tags.add(groupID, filter)
                is Filter.Numeric -> numerics.add(groupID, filter)
            }
        }
    }

    override fun <T : Filter> remove(groupID: FilterGroupID, vararg filters: T) {
        filters.forEach { filter ->
            when (filter) {
                is Filter.Facet -> facets.remove(groupID, filter)
                is Filter.Tag -> tags.remove(groupID, filter)
                is Filter.Numeric -> numerics.remove(groupID, filter)
            }
        }
    }

    override fun <T : Filter> toggle(groupID: FilterGroupID, filter: T) {
        if (contains(groupID, filter)) remove(groupID, filter) else add(groupID, filter)
    }

    override fun clear(groupID: FilterGroupID?) {
        if (groupID != null) {
            facets.clear(groupID)
            numerics.clear(groupID)
            tags.clear(groupID)
        } else {
            facets.clear()
            numerics.clear()
            tags.clear()
        }
    }
}