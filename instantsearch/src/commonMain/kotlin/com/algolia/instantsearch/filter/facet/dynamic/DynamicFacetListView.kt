package com.algolia.instantsearch.filter.facet.dynamic

import com.algolia.instantsearch.filter.Facet

/**
 * View presenting the ordered list of facets and handling user interaction.
 */
public interface DynamicFacetListView {

    /**
     * Update the list of attributed facets.
     */
    public fun setOrderedFacets(facetOrder: List<AttributedFacets>)

    /**
     * Update the facet selections.
     */
    public fun setSelections(selections: SelectionsPerAttribute)

    /**
     * Callback to trigger when user selects a facet.
     */
    public var didSelect: ((attribute: String, Facet) -> Unit)?
}
