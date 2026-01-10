package com.algolia.instantsearch.filter.current.internal

import com.algolia.instantsearch.filter.current.FilterAndID
import com.algolia.instantsearch.filter.state.FilterGroupID
import com.algolia.instantsearch.migration2to3.Filter

internal fun Map<FilterGroupID, Set<Filter>>.toFilterAndIds(): Map<FilterAndID, Filter> {
    return flatMap { (key, value) -> value.map { FilterAndID(key, it) to it } }.toMap()
}
