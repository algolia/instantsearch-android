@file:Suppress("DEPRECATION")

package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.ExperimentalInstantSearch
import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.searcher.debounceFilteringInMillis
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.internal.FacetsSearcherConnectionFilterState
import com.algolia.instantsearch.helper.searcher.internal.HitsSearcherConnectionFilterState
import com.algolia.instantsearch.helper.searcher.internal.SearcherAnswersConnectionFilterState

@ExperimentalInstantSearch
public fun SearcherAnswers.connectFilterState(
    filterState: FilterState,
    debouncer: Debouncer = Debouncer(debounceFilteringInMillis),
): Connection {
    return SearcherAnswersConnectionFilterState(this, filterState, debouncer)
}

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
