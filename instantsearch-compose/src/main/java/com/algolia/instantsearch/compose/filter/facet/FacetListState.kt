package com.algolia.instantsearch.compose.filter.facet

import com.algolia.client.model.search.FacetHits
import com.algolia.instantsearch.compose.filter.facet.internal.FacetListStateImpl
import com.algolia.instantsearch.compose.selectable.list.SelectableListState
import com.algolia.instantsearch.core.selectable.list.SelectableItem
import com.algolia.instantsearch.filter.facet.FacetListView

/**
 * [FacetListView] for compose.
 */
public interface FacetListState : FacetListView, SelectableListState<FacetHits>

/**
 * Creates an instance of [FacetListState].
 *
 * @param facets selectable facets list
 */
public fun FacetListState(facets: List<SelectableItem<FacetHits>> = emptyList()): FacetListState {
    return FacetListStateImpl(facets)
}
