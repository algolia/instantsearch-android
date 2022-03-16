package com.algolia.instantsearch.helper.filter.facet.dynamic

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.helper.filter.facet.dynamic.internal.DynamicFacetListConnectionFilterState
import com.algolia.instantsearch.helper.filter.facet.dynamic.internal.DynamicFacetListConnectionSearcherIndex
import com.algolia.instantsearch.helper.filter.facet.dynamic.internal.DynamicFacetListConnectionView
import com.algolia.instantsearch.helper.filter.state.FilterGroupDescriptor
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherForHits
import com.algolia.search.model.Attribute

/**
 * Establishes connection with a Searcher.
 *
 * @param searcher searcher to connect
 */
public fun DynamicFacetListViewModel.connectSearcher(searcher: SearcherForHits<*>): Connection {
    return DynamicFacetListConnectionSearcherIndex(this, searcher)
}

/**
 * Establishes connection with a FilterState.
 *
 * @param filterState filter state to connect
 * @param filterGroupForAttribute mapping between a facet attribute and a descriptor of a filter group where the
 * corresponding facet filters stored in the filter state. If no filter group descriptor provided, the filters for
 * attribute will be automatically stored in the conjunctive (`and`) group with the facet attribute name.
 */
public fun DynamicFacetListViewModel.connectFilterState(filterState: FilterState, filterGroupForAttribute: Map<Attribute, FilterGroupDescriptor> = emptyMap()): Connection {
    return DynamicFacetListConnectionFilterState(this, filterState, filterGroupForAttribute)
}

/**
 * Establishes connection with a [DynamicFacetListView] implementation.
 *
 * @param view view to connect
 */
public fun DynamicFacetListViewModel.connectView(view: DynamicFacetListView): Connection {
    return DynamicFacetListConnectionView(this, view)
}

/**
 * Establishes a connection with a [DynamicFacetListView] implementation.
 *
 * @param view view to connect
 */
public fun DynamicFacetListConnector.connectView(view: DynamicFacetListView): Connection {
    return viewModel.connectView(view)
}
