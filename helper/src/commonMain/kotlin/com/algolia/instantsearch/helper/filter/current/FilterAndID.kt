package com.algolia.instantsearch.helper.filter.current

import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.search.model.filter.Filter


public typealias FilterAndID = Pair<FilterGroupID, Filter>

internal fun Map<FilterGroupID, Set<Filter>>.toFilterAndIds(): Map<FilterAndID, Filter> {
    return flatMap { (key, value) -> value.map { FilterAndID(key, it) to it } }.toMap()
}