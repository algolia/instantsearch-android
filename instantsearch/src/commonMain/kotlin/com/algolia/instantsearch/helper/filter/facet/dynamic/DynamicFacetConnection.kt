package com.algolia.instantsearch.helper.filter.facet.dynamic

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.helper.filter.facet.dynamic.internal.DynamicFacetConnectionFilterState
import com.algolia.instantsearch.helper.filter.facet.dynamic.internal.DynamicFacetConnectionSearcherIndex
import com.algolia.instantsearch.helper.filter.facet.dynamic.internal.DynamicFacetConnectionView
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherIndex
import com.algolia.search.model.Attribute

/**
 * Establishes connection with a Searcher.
 *
 * @param searcher searcher to connect
 */
public fun DynamicFacetViewModel.connectSearcher(searcher: SearcherIndex<*>): Connection {
    return DynamicFacetConnectionSearcherIndex(this, searcher)
}

/**
 * Establishes connection with a FilterState.
 *
 * @param filterState filter state to connect
 * @param groupIDForAttribute mapping between a facet attribute and a filter group where corresponding facet filters stored in the filter state
 */
public fun DynamicFacetViewModel.connectFilterState(filterState: FilterState, groupIDForAttribute: Map<Attribute, FilterGroupID> = emptyMap()): Connection {
    return DynamicFacetConnectionFilterState(this, filterState, groupIDForAttribute)
}

/**
 * Establishes connection with a DynamicFacetsController implementation.
 *
 * @param view DynamicFacetView to connect
 */
public fun DynamicFacetViewModel.connectView(view: DynamicFacetView): Connection {
    return DynamicFacetConnectionView(this, view)
}
