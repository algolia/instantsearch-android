package com.algolia.instantsearch.searcher

import com.algolia.client.model.search.SearchForFacetValuesResponse
import com.algolia.client.model.search.SearchParams


/**
 * Implementation of [SearcherQuery] with [SearchParams] and [SearchForFacetValuesResponse].
 */
public interface SearcherForFacets<T : SearchParams> : SearcherQuery<T, SearchForFacetValuesResponse> {

    /**
     * Facets attribute.
     */
    public val attribute: String
}
