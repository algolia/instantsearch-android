package com.algolia.instantsearch.compose.filter.facet.internal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.algolia.instantsearch.compose.filter.facet.FacetListState
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.selectable.list.SelectableItem
import com.algolia.search.model.search.Facet

/**
 * [FacetListState] implementation.
 *
 * @param facets initial facets list value
 */
internal class FacetListStateImpl(
    items: List<SelectableItem<Facet>>
) : FacetListState {

    @set:JvmName("_items")
    override var items by mutableStateOf(items)
    override var onSelection: Callback<Facet>? = null

    override fun setItems(items: List<SelectableItem<Facet>>) {
        this.items = items
    }
}
