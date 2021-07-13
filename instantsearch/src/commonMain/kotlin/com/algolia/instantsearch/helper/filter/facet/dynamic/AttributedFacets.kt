package com.algolia.instantsearch.helper.filter.facet.dynamic

import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Facet

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
