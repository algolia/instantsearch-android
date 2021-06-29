package com.algolia.instantsearch.compose.filter.facet.dynamic.internal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.algolia.instantsearch.compose.filter.facet.dynamic.DynamicFacetState
import com.algolia.instantsearch.helper.filter.facet.dynamic.AttributedFacets
import com.algolia.instantsearch.helper.filter.facet.dynamic.SelectionsPerAttribute
import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Facet

/**
 * [DynamicFacetState] implementation.
 *
 * @param facetOrder list of attributed facets to present
 * @param facetSelections mapping between a facet attribute and a set of selected facet values
 */
internal class DynamicFacetStateImpl(
    facetOrder: List<AttributedFacets> = emptyList(),
    facetSelections: SelectionsPerAttribute = emptyMap()
) : DynamicFacetState {

    override var facetOrder: List<AttributedFacets> by mutableStateOf(facetOrder)

    override var facetSelections: SelectionsPerAttribute by mutableStateOf(facetSelections)

    override fun setOrderedFacets(facetOrder: List<AttributedFacets>) {
        this.facetOrder = facetOrder
    }

    override fun setSelections(selections: SelectionsPerAttribute) {
        this.facetSelections = selections
    }

    override fun toggle(facet: Facet, attribute: Attribute) {
        didSelect?.invoke(attribute, facet)
    }

    override fun isSelected(facet: Facet, attribute: Attribute): Boolean {
        return facetSelections[attribute]?.contains(facet.value) == true
    }

    override var didSelect: ((Attribute, Facet) -> Unit)? = null
}
