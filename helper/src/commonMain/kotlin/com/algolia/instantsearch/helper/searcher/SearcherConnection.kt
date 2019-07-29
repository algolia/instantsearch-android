package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.helper.filter.state.FilterState


public fun SearcherSingleIndex.connectionFilterState(
    filterState: FilterState,
    debouncer: Debouncer = Debouncer(0)
): Connection {
    return SearcherSingleConnectionFilterState(this, filterState, debouncer)
}

public fun SearcherForFacets.connectionFilterState(
    filterState: FilterState,
    debouncer: Debouncer = Debouncer(0)
): Connection {
    return SearcherForFacetsConnectionFilterState(this, filterState, debouncer)
}