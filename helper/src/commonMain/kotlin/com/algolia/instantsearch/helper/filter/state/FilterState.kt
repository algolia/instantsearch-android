package com.algolia.instantsearch.helper.filter.state

import com.algolia.instantsearch.core.observable.ObservableItem
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter


public class FilterState internal constructor(
    filters: MutableFilters = MutableFiltersImpl()
) : MutableFilters by filters {

    public constructor() : this(MutableFiltersImpl())

    val filters = ObservableItem(filters)

    internal var hierarchicalAttributes: List<Attribute> = listOf()
    internal var hierarchicalFilters: List<Filter.Facet> = listOf()

    public constructor(map: Map<FilterGroupID, Set<Filter>>) : this() {
        map.forEach { (groupID, filters) -> add(groupID, filters) }
    }

    public fun notify(block: MutableFilters.() -> Unit) {
        block(filters.get())
        notifyChange()
    }

    public fun notifyChange() {
        filters.set(filters.get())
    }

    override fun equals(other: Any?): Boolean {
        return if (other is FilterState) filters.get() == other.filters.get() else false
    }

    override fun hashCode(): Int {
        return filters.hashCode()
    }
}