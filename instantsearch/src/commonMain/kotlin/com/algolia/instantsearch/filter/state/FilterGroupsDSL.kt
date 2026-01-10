package com.algolia.instantsearch.filter.state

import com.algolia.instantsearch.migration2to3.Attribute
import com.algolia.instantsearch.migration2to3.Filter

public class FilterGroupsDSL(
    private val groups: MutableMap<FilterGroupID, Set<Filter>> = mutableMapOf(),
) {

    public fun or(name: String = "", block: DSLGroupFilter.() -> Unit) {
        groups += groupOr(name) to DSLGroupFilter(block)
    }

    public fun or(name: Attribute, block: DSLGroupFilter.() -> Unit) {
        groups += groupOr(name) to DSLGroupFilter(block)
    }

    public fun and(name: String = "", block: DSLGroupFilter.() -> Unit) {
        groups += groupAnd(name) to DSLGroupFilter(block)
    }

    public fun and(name: Attribute, block: DSLGroupFilter.() -> Unit) {
        groups += groupAnd(name) to DSLGroupFilter(block)
    }

    public fun group(groupID: FilterGroupID, block: DSLGroupFilter.() -> Unit) {
        groups += groupID to DSLGroupFilter(block)
    }

    public companion object : DSL<FilterGroupsDSL, Map<FilterGroupID, Set<Filter>>> {

        override operator fun invoke(block: FilterGroupsDSL.() -> Unit): Map<FilterGroupID, Set<Filter>> {
            return FilterGroupsDSL().apply(block).groups
        }
    }
}
