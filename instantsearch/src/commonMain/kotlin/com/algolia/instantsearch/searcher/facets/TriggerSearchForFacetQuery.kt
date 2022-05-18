package com.algolia.instantsearch.searcher.facets

import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Query

/**
 * Facets search query execution condition logic.
 */
public fun interface TriggerSearchForFacetQuery {

    /**
     * Search trigger logic. Return `true` to run search operations.
     *
     * @param query query for search operations
     * @param attribute facets attribute
     * @param facetQuery facets query
     */
    public fun trigger(query: Query, attribute: Attribute, facetQuery: String?): Boolean
}
