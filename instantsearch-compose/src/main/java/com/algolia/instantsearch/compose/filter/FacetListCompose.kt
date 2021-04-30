package com.algolia.instantsearch.compose.filter

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.algolia.instantsearch.compose.filter.internal.FacetListComposeImpl
import com.algolia.instantsearch.core.selectable.list.SelectableItem
import com.algolia.instantsearch.helper.filter.facet.FacetListView
import com.algolia.search.model.search.Facet

/**
 * [FacetListView] for compose.
 */
public interface FacetListCompose : FacetListView {

    /**
     * State holding selectable facets list value.
     */
    public val facets: State<List<SelectableItem<Facet>>>
}

/**
 * Creates an instance of [FacetListCompose].
 *
 * @param facets state holding selectable facets list value
 */
public fun FacetListCompose(facets: MutableState<List<SelectableItem<Facet>>>): FacetListCompose {
    return FacetListComposeImpl(facets)
}

/**
 * Creates an instance of [FacetListCompose].
 *
 * @param facets selectable facets list
 */
public fun FacetListCompose(facets: List<SelectableItem<Facet>> = emptyList()): FacetListCompose {
    return FacetListComposeImpl(mutableStateOf(facets))
}
