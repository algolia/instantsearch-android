package com.algolia.instantsearch.android.filter.facet.dynamic

import com.algolia.instantsearch.migration2to3.Attribute
import com.algolia.instantsearch.migration2to3.Facet


/**
 * Facet view model to be rendered.
 */
public sealed class DynamicFacetModel {

    /**
     * Attribute view to be rendered.
     */
    public data class Header(val attribute: Attribute) : DynamicFacetModel()

    /**
     * Facet value view to be rendered.
     */
    public data class Item(val attribute: Attribute, val facet: Facet, val selected: Boolean) : DynamicFacetModel()
}
