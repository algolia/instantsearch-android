package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.autoConnect
import com.algolia.instantsearch.helper.filter.state.FilterState


public fun SearcherSingleIndex.connectFilterState(
    filterState: FilterState,
    connect: Boolean = true
): Connection {
    return SearcherSingleConnectionFilterState(this, filterState).autoConnect(connect)
}

public fun SearcherForFacets.connectFilterState(
    filterState: FilterState,
    connect: Boolean = true
): Connection {
    return SearcherForFacetsConnectionFilterState(this, filterState).autoConnect(connect)
}