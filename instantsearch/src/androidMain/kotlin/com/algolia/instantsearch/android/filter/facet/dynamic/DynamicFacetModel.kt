package com.algolia.instantsearch.android.filter.facet.dynamic

import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Facet

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
