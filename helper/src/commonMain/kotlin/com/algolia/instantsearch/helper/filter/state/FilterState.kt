package com.algolia.instantsearch.helper.filter.state

import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.search.model.filter.Filter


public class FilterState internal constructor(
    filters: MutableFilters = MutableFiltersImpl()
) : MutableFilters by filters {

    public val filters = SubscriptionValue<Filters>(filters)

    public constructor() : this(MutableFiltersImpl())

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