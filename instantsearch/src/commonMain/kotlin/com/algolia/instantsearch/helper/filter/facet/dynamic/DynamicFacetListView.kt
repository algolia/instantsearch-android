package com.algolia.instantsearch.helper.filter.facet.dynamic

import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Facet

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
    public var didSelect: ((Attribute, Facet) -> Unit)?
}
