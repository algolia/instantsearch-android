package com.algolia.instantsearch.filter.current

import com.algolia.instantsearch.filter.state.FilterGroupID
import com.algolia.instantsearch.filter.Filter

public typealias FilterAndID = Pair<FilterGroupID, Filter>

internal fun Map<FilterGroupID, Set<Filter>>.toFilterAndIds(): Map<FilterAndID, Filter> {
    return flatMap { (key, value) -> value.map { FilterAndID(key, it) to it } }.toMap()
}
