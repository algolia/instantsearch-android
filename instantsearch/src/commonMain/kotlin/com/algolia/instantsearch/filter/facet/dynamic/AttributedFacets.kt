package com.algolia.instantsearch.filter.facet.dynamic


import com.algolia.instantsearch.filter.Facet


/**
 * List of ordered facets with their attributes.
 */
public data class AttributedFacets(
    /**
     * Facet attribute.
     */
    public val attribute: String,
    /**
     * List of ordered facet values.
     */
    public val facets: List<Facet>
)
