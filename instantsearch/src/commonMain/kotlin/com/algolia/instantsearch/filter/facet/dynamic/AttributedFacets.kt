package com.algolia.instantsearch.filter.facet.dynamic

import com.algolia.instantsearch.migration2to3.Attribute
import com.algolia.instantsearch.migration2to3.Facet


/**
 * List of ordered facets with their attributes.
 */
public data class AttributedFacets(
    /**
     * Facet attribute.
     */
    public val attribute: Attribute,
    /**
     * List of ordered facet values.
     */
    public val facets: List<Facet>
)
