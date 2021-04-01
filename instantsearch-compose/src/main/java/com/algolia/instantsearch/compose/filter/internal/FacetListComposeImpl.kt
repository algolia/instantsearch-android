package com.algolia.instantsearch.compose.filter.internal

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import com.algolia.instantsearch.compose.filter.FacetListCompose
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.selectable.list.SelectableItem
import com.algolia.search.model.search.Facet

internal class FacetListComposeImpl(
    private val mutableFacets: MutableState<List<SelectableItem<Facet>>>
) : FacetListCompose {

    override val facets: State<List<SelectableItem<Facet>>>
        get() = mutableFacets

    override var onSelection: Callback<Facet>? = null

    override fun setItems(items: List<SelectableItem<Facet>>) {
        this.mutableFacets.value = items
    }
}
