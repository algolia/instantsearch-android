package com.algolia.instantsearch.compose.filter.list.internal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.algolia.instantsearch.compose.filter.list.FacetListState
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.selectable.list.SelectableItem
import com.algolia.search.model.search.Facet

/**
 * [FacetListState] implementation.
 *
 * @param facets state holding selectable facets list value
 */
internal class FacetListStateImpl(
    facets: List<SelectableItem<Facet>>
) : FacetListState {

    override var facets by mutableStateOf(facets)
    override var onSelection: Callback<Facet>? = null

    override fun setItems(items: List<SelectableItem<Facet>>) {
        this.facets = items
    }
}
