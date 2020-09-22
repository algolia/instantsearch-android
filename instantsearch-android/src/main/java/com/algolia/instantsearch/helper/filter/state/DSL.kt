package com.algolia.instantsearch.helper.filter.state

import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter

public fun groupOr(attribute: Attribute): FilterGroupID {
    return groupOr(attribute.raw)
}

public fun groupOr(name: String = ""): FilterGroupID {
    return FilterGroupID(name, FilterOperator.Or)
}

public fun groupAnd(attribute: Attribute): FilterGroupID {
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
