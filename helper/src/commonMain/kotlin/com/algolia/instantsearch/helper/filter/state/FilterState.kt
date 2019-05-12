package com.algolia.instantsearch.helper.filter.state

import com.algolia.search.model.filter.Filter


public class FilterState(
    internal val filters: MutableFilters = MutableFiltersImpl()
) : MutableFilters by filters {

    public constructor(
        facetGroups: MutableMap<FilterGroupID, Set<Filter.Facet>> = mutableMapOf(),
        tagGroups: MutableMap<FilterGroupID, Set<Filter.Tag>> = mutableMapOf(),
        numericGroups: MutableMap<FilterGroupID, Set<Filter.Numeric>> = mutableMapOf()
    ) : this(MutableFiltersImpl(facetGroups, tagGroups, numericGroups))

    public val onChange: MutableList<(Filters) -> Unit> = mutableListOf()

    public fun notify(block: MutableFilters.() -> Unit) {
        block(filters)
        notifyChange()
    }

    public fun notifyChange() {
        onChange.forEach { it(filters) }
    }

    override fun toString(): String =
        "FilterState(filters=$filters, onStateChanged=${onChange.size} listener${if (onChange.size > 1) "s" else ""})"
}