package com.algolia.instantsearch.helper.filter.state

import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter


public class FilterState internal constructor(
    internal val filters: MutableFilters = MutableFiltersImpl()
) : MutableFilters by filters {

    public constructor(): this(MutableFiltersImpl())

    public constructor(map: Map<FilterGroupID, Set<Filter>>) : this() {
        map.forEach { (groupID, filters) -> add(groupID, filters) }
    }

    internal var hierarchicalAttributes: List<Attribute> = listOf()
    internal var hierarchicalFilters: List<Filter.Facet> = listOf()

    public val onChanged: MutableList<(Filters) -> Unit> = mutableListOf()

    public fun notify(block: MutableFilters.() -> Unit) {
        block(filters)
        notifyChange()
    }

    public fun notifyChange() {
        onChanged.forEach { it(filters) }
    }

    override fun equals(other: Any?): Boolean {
        return if (other is FilterState) filters == other.filters else false
    }

    override fun hashCode(): Int {
        return filters.hashCode()
    }

    override fun toString(): String =
        "FilterState(filters=$filters, onStateChanged=${onChanged.size} listener${if (onChanged.size > 1) "s" else ""})"
}