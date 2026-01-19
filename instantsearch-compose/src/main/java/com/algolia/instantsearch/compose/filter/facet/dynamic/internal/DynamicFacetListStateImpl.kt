package com.algolia.instantsearch.compose.filter.facet.dynamic.internal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.algolia.instantsearch.compose.filter.facet.dynamic.DynamicFacetListState
import com.algolia.instantsearch.filter.facet.dynamic.AttributedFacets
import com.algolia.instantsearch.filter.facet.dynamic.SelectionsPerAttribute

import com.algolia.instantsearch.filter.Facet

/**
 * [DynamicFacetListState] implementation.
 *
 * @param orderedFacets list of attributed facets to present
 * @param selections mapping between a facet attribute and a set of selected facet values
 */
internal class DynamicFacetListStateImpl(
    orderedFacets: List<AttributedFacets> = emptyList(),
    selections: SelectionsPerAttribute = emptyMap()
) : DynamicFacetListState {

    @set:JvmName("_selections")
    override var selections: SelectionsPerAttribute by mutableStateOf(selections)

    @set:JvmName("_orderedFacets")
    override var orderedFacets: List<AttributedFacets> by mutableStateOf(orderedFacets)

    override var didSelect: ((Attribute, Facet) -> Unit)? = null

    override fun setOrderedFacets(facetOrder: List<AttributedFacets>) {
        this.orderedFacets = facetOrder
    }

    override fun setSelections(selections: SelectionsPerAttribute) {
        this.selections = selections
    }

    override fun toggle(facet: Facet, attribute: String) {
        didSelect?.invoke(attribute, facet)
    }

    override fun isSelected(facet: Facet, attribute: String): Boolean {
        return selections[attribute]?.contains(facet.value) == true
    }
}
