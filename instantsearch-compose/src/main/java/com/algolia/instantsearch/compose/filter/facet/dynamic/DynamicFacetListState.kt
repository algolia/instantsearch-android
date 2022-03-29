package com.algolia.instantsearch.compose.filter.facet.dynamic

import com.algolia.instantsearch.compose.filter.facet.dynamic.internal.DynamicFacetListStateImpl
import com.algolia.instantsearch.filter.facet.dynamic.AttributedFacets
import com.algolia.instantsearch.filter.facet.dynamic.DynamicFacetListView
import com.algolia.instantsearch.filter.facet.dynamic.SelectionsPerAttribute
import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Facet

/**
 * [DynamicFacetListView] for compose.
 */
public interface DynamicFacetListState : DynamicFacetListView {

    /**
     * List of attributed facets to present.
     */
    public val orderedFacets: List<AttributedFacets>

    /**
     * Mapping between a facet attribute and a set of selected facet values.
     */
    public val selections: SelectionsPerAttribute

    /**
     * Toggle facet selection.
     *
     * @param facet selected facet
     * @param attribute facet attribute
     */
    public fun toggle(facet: Facet, attribute: Attribute)

    /**
     * Check if the provided facet for attribute is selected.
     *
     * @param facet selected facet
     * @param attribute facet attribute
     * @return true value if the provided facet for attribute is selected, otherwise false.
     */
    public fun isSelected(facet: Facet, attribute: Attribute): Boolean
}

/**
 * Creates an instance of [DynamicFacetListState].
 *
 * @param orderedFacets list of attributed facets to present
 * @param selections mapping between a facet attribute and a set of selected facet values
 */
public fun DynamicFacetListState(
    orderedFacets: List<AttributedFacets> = emptyList(),
    selections: SelectionsPerAttribute = emptyMap()
): DynamicFacetListState {
    return DynamicFacetListStateImpl(orderedFacets, selections)
}
