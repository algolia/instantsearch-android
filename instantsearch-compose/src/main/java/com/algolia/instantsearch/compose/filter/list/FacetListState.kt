package com.algolia.instantsearch.compose.filter.list

import com.algolia.instantsearch.compose.filter.list.internal.FacetListStateImpl
import com.algolia.instantsearch.core.selectable.list.SelectableItem
import com.algolia.instantsearch.helper.filter.facet.FacetListView
import com.algolia.search.model.search.Facet

/**
 * [FacetListView] for compose.
 */
public interface FacetListState : FacetListView {

    /**
     * State holding selectable facets list value.
     */
    public val facets: List<SelectableItem<Facet>>
}

/**
 * Creates an instance of [FacetListState].
 *
 * @param facets selectable facets list
 */
public fun FacetListState(facets: List<SelectableItem<Facet>> = emptyList()): FacetListState {
    return FacetListStateImpl(facets)
}
