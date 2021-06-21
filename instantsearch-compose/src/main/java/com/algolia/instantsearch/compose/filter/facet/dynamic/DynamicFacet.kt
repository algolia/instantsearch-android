package com.algolia.instantsearch.compose.filter.facet.dynamic

import com.algolia.instantsearch.compose.filter.facet.dynamic.internal.DynamicFacetImpl
import com.algolia.instantsearch.helper.filter.facet.dynamic.AttributedFacets
import com.algolia.instantsearch.helper.filter.facet.dynamic.DynamicFacetView
import com.algolia.instantsearch.helper.filter.facet.dynamic.SelectionsPerAttribute
import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Facet

/**
 * [DynamicFacetView] for compose.
 */
public interface DynamicFacet : DynamicFacetView {

    /**
     * List of attributed facets.
     */
    public val facetOrder: List<AttributedFacets>

    /**
     * Mapping between a facet attribute and a set of selected facet values.
     */
    public val facetSelections: SelectionsPerAttribute

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
 * Creates an instance of [DynamicFacet].
 *
 * @param facetOrder list of attributed facets to present
 * @param facetSelections mapping between a facet attribute and a set of selected facet values
 */
public fun DynamicFacet(
    facetOrder: List<AttributedFacets> = emptyList(),
    facetSelections: SelectionsPerAttribute = emptyMap()
): DynamicFacet {
    return DynamicFacetImpl(facetOrder, facetSelections)
}
