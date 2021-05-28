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

    override fun setItems(items: List<SelectableItem<Facet>>) {
        this.facets = items
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as FacetListComposeImpl
        if (facets != other.facets) return false
        if (onSelection != other.onSelection) return false
        return true
    }

    override fun hashCode(): Int {
        var result = facets.hashCode()
        result = 31 * result + (onSelection?.hashCode() ?: 0)
        return result
    }
}
