package com.algolia.instantsearch.searcher.facets

import com.algolia.client.model.search.SearchParamsObject
import com.algolia.instantsearch.migration2to3.Attribute


/**
 * Facets search query execution condition logic.
 */
public fun interface SearchForFacetQuery {

    /**
     * Search trigger logic. Return `true` to run search operations.
     *
     * @param query query for search operations
     * @param attribute facets attribute
     * @param facetQuery facets query
     */
    public fun trigger(query: SearchParamsObject, attribute: Attribute, facetQuery: String?): Boolean

    public companion object {


        /**
         * Trigger search for all queries.
         */
        public val All: SearchForFacetQuery = SearchForFacetQuery { _, _, _ -> true }

        /**
         * Trigger if the facet query length is greater or equals to [length].
         *
         * @param length minimal query length
         */
        public fun lengthAtLeast(length: Int): SearchForFacetQuery = SearchForFacetQuery { _, _, facetQuery ->
            (facetQuery?.length ?: 0) >= length
        }
    }
}
