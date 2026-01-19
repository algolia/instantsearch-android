package com.algolia.instantsearch.filter.state


import com.algolia.instantsearch.filter.Filter


public fun groupOr(attribute: String): FilterGroupID {
    return groupOr(attribute.raw)
}

public fun groupOr(name: String = ""): FilterGroupID {
    return FilterGroupID(name, FilterOperator.Or)
}

public fun groupAnd(attribute: String): FilterGroupID {
    return groupAnd(attribute.raw)
}

public fun groupAnd(name: String = ""): FilterGroupID {
    return FilterGroupID(name)
}

public fun filters(block: FilterGroupsDSL.() -> Unit): Map<FilterGroupID, Set<Filter>> {
    return FilterGroupsDSL(block)
}

public fun filterState(block: FilterGroupsDSL.() -> Unit): FilterState {
    return FilterState(filters(block))
}
