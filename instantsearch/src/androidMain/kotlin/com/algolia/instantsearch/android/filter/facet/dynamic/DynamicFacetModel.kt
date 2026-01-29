package com.algolia.instantsearch.android.filter.facet.dynamic

import com.algolia.client.model.search.FacetHits

/**
 * Facet view model to be rendered.
 */
public sealed class DynamicFacetModel {

    /**
     * Attribute view to be rendered.
     */
    public data class Header(val attribute: String) : DynamicFacetModel()

    /**
     * Facet value view to be rendered.
     */
    public data class Item(val attribute: String, val facet: FacetHits, val selected: Boolean) : DynamicFacetModel()
}
