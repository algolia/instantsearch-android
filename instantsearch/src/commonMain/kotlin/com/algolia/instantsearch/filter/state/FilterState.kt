package com.algolia.instantsearch.filter.state

import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.extension.traceFilterState
import com.algolia.instantsearch.filter.state.internal.DefaultMutableFilters
import com.algolia.instantsearch.filter.Filter

/**
 * A FilterState is a class that holds one or several filters, organized in groups. The FilterState can be modified
 * at any moment by adding or removing filters, which will be applied to searches performed by the connected Searcher.
 * [Documentation](https://www.algolia.com/doc/api-reference/widgets/filter-state/android/)
 */
public class FilterState internal constructor(
    filters: MutableFilters = DefaultMutableFilters(),
) : MutableFilters by filters {

    public val filters: SubscriptionValue<Filters> = SubscriptionValue(filters)

    public constructor() : this(DefaultMutableFilters())

    init {
        traceFilterState()
    }

    public constructor(map: Map<FilterGroupID, Set<Filter>>) : this() {
        map.forEach { (groupID, filters) -> add(groupID, filters) }
    }

    public fun notify(block: MutableFilters.() -> Unit) {
        block(this)
        notifyChange()
    }

    public fun notifyChange() {
        filters.notifySubscriptions()
    }

    override fun equals(other: Any?): Boolean {
        return if (other is FilterState) filters.value == other.filters.value else false
    }

    override fun hashCode(): Int {
        return filters.hashCode()
    }
}
