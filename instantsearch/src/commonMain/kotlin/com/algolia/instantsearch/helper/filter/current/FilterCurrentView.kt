package com.algolia.instantsearch.helper.filter.current

import com.algolia.instantsearch.core.Callback

public interface FilterCurrentView {

    public var onFilterSelected: Callback<FilterAndID>?

    public fun setFilters(filters: List<Pair<FilterAndID, String>>)
}
