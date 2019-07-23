package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.autoConnect
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.helper.filter.state.FilterState


public fun SearcherSingleIndex.connectFilterState(
    filterState: FilterState,
    debouncer: Debouncer = Debouncer(0),
    connect: Boolean = true
): Connection {
    return SearcherSingleConnectionFilterState(this, filterState, debouncer).autoConnect(connect)
}

public fun SearcherForFacets.connectFilterState(
    filterState: FilterState,
    debouncer: Debouncer = Debouncer(0),
    connect: Boolean = true
): Connection {
    return SearcherForFacetsConnectionFilterState(this, filterState, debouncer).autoConnect(connect)
}

public fun SearcherSingleIndex.connectionFilterState(
    filterState: FilterState,
    debouncer: Debouncer
): Connection {
    return SearcherSingleConnectionFilterState(this, filterState, debouncer)
}

public fun SearcherForFacets.connectionFilterState(
    filterState: FilterState,
    debouncer: Debouncer
): Connection {
    return SearcherForFacetsConnectionFilterState(this, filterState, debouncer)
}

public fun SearcherSingleIndex.with(
    filterState: FilterState,
    debouncer: Debouncer = Debouncer(0)
): Connection {
    return connectionFilterState(filterState, debouncer).apply { connect() }
}

public fun SearcherForFacets.with(
    filterState: FilterState,
    debouncer: Debouncer = Debouncer(0)
): Connection {
    return connectionFilterState(filterState, debouncer).apply { connect() }
}