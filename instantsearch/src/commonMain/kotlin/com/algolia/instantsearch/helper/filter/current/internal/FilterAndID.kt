package com.algolia.instantsearch.helper.filter.current.internal

import com.algolia.instantsearch.helper.filter.current.FilterAndID
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.search.model.filter.Filter

internal fun Map<FilterGroupID, Set<Filter>>.toFilterAndIds(): Map<FilterAndID, Filter> {
    return flatMap { (key, value) -> value.map { FilterAndID(key, it) to it } }.toMap()
}
