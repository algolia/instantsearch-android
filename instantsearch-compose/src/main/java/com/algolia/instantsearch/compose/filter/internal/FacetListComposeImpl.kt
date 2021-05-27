package com.algolia.instantsearch.compose.filter.internal

import androidx.compose.runtime.MutableState
import com.algolia.instantsearch.compose.filter.FacetListCompose
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.selectable.list.SelectableItem
import com.algolia.search.model.search.Facet

/**
 * [FacetListCompose] implementation.
 *
 * @param facets state holding selectable facets list value
 */
internal class FacetListComposeImpl(
    override val facets: MutableState<List<SelectableItem<Facet>>>
) : FacetListCompose {

    override var onSelection: Callback<Facet>? = null

    override fun setItems(items: List<SelectableItem<Facet>>) {
        this.facets.value = items
    }
}
