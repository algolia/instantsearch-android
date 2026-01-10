package com.algolia.instantsearch.filter.state.internal

import com.algolia.instantsearch.filter.state.FilterGroupID
import com.algolia.instantsearch.filter.state.Filters
import com.algolia.instantsearch.filter.state.MutableFilters
import com.algolia.instantsearch.filter.state.add
import com.algolia.instantsearch.hierarchical.HierarchicalFilter
import com.algolia.instantsearch.migration2to3.Attribute
import com.algolia.instantsearch.migration2to3.Filter

internal data class DefaultMutableFilters(
    private val facetGroups: MutableMap<FilterGroupID, Set<Filter.Facet>> = mutableMapOf(),
    private val tagGroups: MutableMap<FilterGroupID, Set<Filter.Tag>> = mutableMapOf(),
    private val numericGroups: MutableMap<FilterGroupID, Set<Filter.Numeric>> = mutableMapOf(),
    private val hierarchicalGroups: MutableMap<Attribute, HierarchicalFilter> = mutableMapOf(),
) : MutableFilters, Filters by FiltersImpl(facetGroups, tagGroups, numericGroups, hierarchicalGroups) {

    private fun <T : Filter> Map<FilterGroupID, Set<T>>.getOrDefault(groupID: FilterGroupID): Set<T> {
        return getOrElse(groupID) { setOf() }
    }

    private fun <T : Filter> MutableMap<FilterGroupID, Set<T>>.modify(
        groupID: FilterGroupID,
        filter: T,
        operator: Set<T>.(T) -> Set<T>,
    ) {
        val set = getOrDefault(groupID).operator(filter)

        if (set.isNotEmpty()) this[groupID] = set else remove(groupID)
    }

    private fun <T : Filter> MutableMap<FilterGroupID, Set<T>>.add(groupID: FilterGroupID, filter: T) {
        modify(groupID, filter) { plus(it) }
    }

    private fun <T : Filter> MutableMap<FilterGroupID, Set<T>>.remove(groupID: FilterGroupID, filter: T) {
        modify(groupID, filter) { minus(it) }
    }

    private fun <T : Filter> MutableMap<FilterGroupID, Set<T>>.clear(groupID: FilterGroupID) {
        remove(groupID)
    }

    override fun set(map: Map<FilterGroupID, Set<Filter>>) {
        clear()
        map.forEach { (groupID, filter) ->
            add(groupID, filter)
        }
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

    override fun remove(attribute: Attribute) {
        hierarchicalGroups.remove(attribute)
    }

    override fun <T : Filter> toggle(groupID: FilterGroupID, filter: T) {
        if (contains(groupID, filter)) remove(groupID, filter) else add(groupID, filter)
    }

    override fun clear(vararg groupIDs: FilterGroupID) {
        if (groupIDs.isNotEmpty()) {
            groupIDs.forEach {
                facetGroups.clear(it)
                numericGroups.clear(it)
                tagGroups.clear(it)
            }
        } else {
            facetGroups.clear()
            numericGroups.clear()
            tagGroups.clear()
            hierarchicalGroups.clear()
        }
    }

    override fun clearExcept(groupIDs: List<FilterGroupID>) {
        facetGroups.filter { it.key !in groupIDs }.forEach { facetGroups.remove(it.key) }
        numericGroups.filter { it.key !in groupIDs }.forEach { numericGroups.remove(it.key) }
        tagGroups.filter { it.key !in groupIDs }.forEach { tagGroups.remove(it.key) }
    }

    override fun add(attribute: Attribute, hierarchicalFilter: HierarchicalFilter) {
        hierarchicalGroups[attribute] = hierarchicalFilter
    }
}
