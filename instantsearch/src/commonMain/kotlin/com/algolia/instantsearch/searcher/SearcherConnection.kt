package com.algolia.instantsearch.searcher

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.searcher.debounceFilteringInMillis
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.searcher.internal.HitsSearcherConnectionFilterState
import com.algolia.instantsearch.searcher.internal.FacetsSearcherConnectionFilterState

/**
 * Connection between hits searcher (w/ filter groups) capabilities and filter state.
 *
 * @param filterState the [FilterState] holding your filters
 * @param debouncer delays searcher operations by a specified time duration.
 */
public fun <S> S.connectFilterState(
    filterState: FilterState,
    debouncer: Debouncer = Debouncer(debounceFilteringInMillis),
): Connection where S : SearcherForHits<*>, S : FilterGroupsHolder {
    return HitsSearcherConnectionFilterState(this, filterState, debouncer)
}

/**
 * Connection between searcher for facets and filter state.
 *
 * @param filterState the [FilterState] holding your filters
 * @param debouncer delays searcher operations by a specified time duration.
 */
public fun SearcherForFacets<*>.connectFilterState(
    filterState: FilterState,
    debouncer: Debouncer = Debouncer(debounceFilteringInMillis),
): Connection {
    return FacetsSearcherConnectionFilterState(this, filterState, debouncer)
}
