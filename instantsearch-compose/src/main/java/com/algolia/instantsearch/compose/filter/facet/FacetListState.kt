package com.algolia.instantsearch.compose.filter.facet

import com.algolia.instantsearch.compose.filter.facet.internal.FacetListStateImpl
import com.algolia.instantsearch.compose.selectable.list.SelectableListState
import com.algolia.instantsearch.core.selectable.list.SelectableItem
import com.algolia.instantsearch.filter.facet.FacetListView
import com.algolia.instantsearch.migration2to3.Facet

/**
 * [FacetListView] for compose.
 */
public interface FacetListState : FacetListView, SelectableListState<Facet>

/**
 * Creates an instance of [FacetListState].
 *
 * @param facets selectable facets list
 */
public fun FacetListState(facets: List<SelectableItem<Facet>> = emptyList()): FacetListState {
    return FacetListStateImpl(facets)
}
