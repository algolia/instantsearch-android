package com.algolia.instantsearch.compose.filter

import androidx.compose.runtime.Stable
import com.algolia.instantsearch.compose.filter.internal.FacetListComposeImpl
import com.algolia.instantsearch.core.selectable.list.SelectableItem
import com.algolia.instantsearch.helper.filter.facet.FacetListView
import com.algolia.search.model.search.Facet

/**
 * [FacetListView] for compose.
 */
@Stable
public interface FacetListCompose : FacetListView {

    /**
     * State holding selectable facets list value.
     */
    public var facets: List<SelectableItem<Facet>>

    override fun setItems(items: List<SelectableItem<Facet>>) {
        this.facets = items
    }
}

/**
 * Creates an instance of [FacetListCompose].
 *
 * @param facets selectable facets list
 */
public fun FacetListCompose(facets: List<SelectableItem<Facet>> = emptyList()): FacetListCompose {
    return FacetListComposeImpl(facets)
}
