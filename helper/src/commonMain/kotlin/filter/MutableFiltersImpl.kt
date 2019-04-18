package filter

import com.algolia.search.model.filter.Filter
import search.GroupID


internal class MutableFiltersImpl(
    private val facets: MutableMap<GroupID, Set<Filter.Facet>> = mutableMapOf(),
    private val tags: MutableMap<GroupID, Set<Filter.Tag>> = mutableMapOf(),
    private val numerics: MutableMap<GroupID, Set<Filter.Numeric>> = mutableMapOf()
) : MutableFilters, Filters by FiltersImpl(facets, tags, numerics) {

    private fun <T : Filter> Map<GroupID, Set<T>>.getOrDefault(groupID: GroupID): Set<T> {
        return getOrElse(groupID, { setOf() })
    }

    private fun <T : Filter> MutableMap<GroupID, Set<T>>.modify(
        groupID: GroupID,
        filter: T,
        operator: Set<T>.(T) -> Set<T>
    ) {
        val set = getOrDefault(groupID).operator(filter)

        if (set.isNotEmpty()) this[groupID] = set else remove(groupID)
    }

    private fun <T : Filter> MutableMap<GroupID, Set<T>>.add(groupID: GroupID, filter: T) {
        modify(groupID, filter, { plus(it) })
    }

    private fun <T : Filter> MutableMap<GroupID, Set<T>>.remove(groupID: GroupID, filter: T) {
        modify(groupID, filter, { minus(it) })
    }

    private fun <T : Filter> MutableMap<GroupID, Set<T>>.clear(groupID: GroupID) {
        remove(groupID)
    }

    override fun <T : Filter> add(groupID: GroupID, vararg filters: T) {
        filters.forEach { filter ->
            when (filter) {
                is Filter.Facet -> facets.add(groupID, filter)
                is Filter.Tag -> tags.add(groupID, filter)
                is Filter.Numeric -> numerics.add(groupID, filter)
            }
        }
    }

    override fun <T : Filter> remove(groupID: GroupID, vararg filters: T) {
        filters.forEach { filter ->
            when (filter) {
                is Filter.Facet -> facets.remove(groupID, filter)
                is Filter.Tag -> tags.remove(groupID, filter)
                is Filter.Numeric -> numerics.remove(groupID, filter)
            }
        }
    }

    override fun <T : Filter> toggle(groupID: GroupID, filter: T) {
        if (contains(groupID, filter)) remove(groupID, filter) else add(groupID, filter)
    }

    override fun clear(groupID: GroupID?) {
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