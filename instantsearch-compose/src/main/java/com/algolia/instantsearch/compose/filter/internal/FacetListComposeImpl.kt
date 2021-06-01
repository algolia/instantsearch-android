package com.algolia.instantsearch.compose.filter.internal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    facets: List<SelectableItem<Facet>>
) : FacetListCompose {

    override var facets by mutableStateOf(facets)
    override var onSelection: Callback<Facet>? = null
}
