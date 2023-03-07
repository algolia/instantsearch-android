package com.algolia.instantsearch.filter.facet.dynamic

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.filter.facet.dynamic.internal.DynamicFacetListConnectionFilterState
import com.algolia.instantsearch.filter.facet.dynamic.internal.DynamicFacetListConnectionSearcherIndex
import com.algolia.instantsearch.filter.facet.dynamic.internal.DynamicFacetListConnectionView
import com.algolia.instantsearch.filter.state.FilterGroupDescriptor
import com.algolia.instantsearch.filter.state.FilterOperator
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.searcher.SearcherForHits
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
 * attribute will be automatically stored in the group of `defaultFilterOperator` type with the facet attribute name.
 */
public fun DynamicFacetListViewModel.connectFilterState(
    filterState: FilterState,
    filterGroupForAttribute: Map<Attribute, FilterGroupDescriptor> = emptyMap(),
    defaultFilterOperator: FilterOperator = FilterOperator.And,
): Connection =
    DynamicFacetListConnectionFilterState(this, filterState, filterGroupForAttribute, defaultFilterOperator)

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
