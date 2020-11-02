package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.searcher.debounceFilteringInMillis
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.internal.SearcherForFacetsConnectionFilterState
import com.algolia.instantsearch.helper.searcher.internal.SearcherMultipleConnectionFilterState
import com.algolia.instantsearch.helper.searcher.internal.SearcherSingleConnectionFilterState
import com.algolia.search.model.IndexName

public fun SearcherSingleIndex.connectFilterState(
    filterState: FilterState,
    debouncer: Debouncer = Debouncer(debounceFilteringInMillis),
): Connection {
    return SearcherSingleConnectionFilterState(this, filterState, debouncer)
}

public fun SearcherForFacets.connectFilterState(
    filterState: FilterState,
    debouncer: Debouncer = Debouncer(debounceFilteringInMillis),
): Connection {
    return SearcherForFacetsConnectionFilterState(this, filterState, debouncer)
}

public fun SearcherMultipleIndex.connectFilterState(
    filterState: FilterState,
    indexName: IndexName,
    debouncer: Debouncer = Debouncer(debounceFilteringInMillis),
): Connection {
    return SearcherMultipleConnectionFilterState(this, filterState, indexName, debouncer)
}
