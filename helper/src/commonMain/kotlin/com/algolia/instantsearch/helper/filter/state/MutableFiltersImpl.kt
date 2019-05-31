package com.algolia.instantsearch.helper.filter.state

import com.algolia.search.model.filter.Filter


internal data class MutableFiltersImpl(
    private val facetGroups: MutableMap<FilterGroupID, Set<Filter.Facet>> = mutableMapOf(),
    private val tagGroups: MutableMap<FilterGroupID, Set<Filter.Tag>> = mutableMapOf(),
    private val numericGroups: MutableMap<FilterGroupID, Set<Filter.Numeric>> = mutableMapOf()
) : MutableFilters, Filters by FiltersImpl(facetGroups, tagGroups, numericGroups) {

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
                is Filter.Facet -> facetGroups.add(groupID, filter)
                is Filter.Tag -> tagGroups.add(groupID, filter)
                is Filter.Numeric -> numericGroups.add(groupID, filter)
            }
        }
    }

    override fun <T : Filter> remove(groupID: FilterGroupID, vararg filters: T) {
        filters.forEach { filter ->
            when (filter) {
                is Filter.Facet -> facetGroups.remove(groupID, filter)
                is Filter.Tag -> tagGroups.remove(groupID, filter)
                is Filter.Numeric -> numericGroups.remove(groupID, filter)
            }
        }
    }

    override fun <T : Filter> toggle(groupID: FilterGroupID, filter: T) {
        if (contains(groupID, filter)) remove(groupID, filter) else add(groupID, filter)
    }

    override fun clear(
        exceptGroup: Boolean,
        vararg groupID: FilterGroupID
    ) {
        if (groupID.isNotEmpty()) {
            if (exceptGroup) {
                facetGroups.filter { it.key !in groupID }.forEach { facetGroups.remove(it.key) }
                numericGroups.filter { it.key !in groupID }.forEach { numericGroups.remove(it.key) }
                tagGroups.filter { it.key !in groupID }.forEach { tagGroups.remove(it.key) }
            } else {
                groupID.forEach {
                    facetGroups.clear(it)
                    numericGroups.clear(it)
                    tagGroups.clear(it)
                }
            }
        } else {
            facetGroups.clear()
            numericGroups.clear()
            tagGroups.clear()
        }
    }

    override fun toString(): String {
        return "MutableFiltersImpl(facets=$facetGroups, tags=$tagGroups, numerics=$numericGroups)"
    }
}