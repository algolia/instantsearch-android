package com.algolia.instantsearch.searcher.hits

import com.algolia.search.model.search.Query

/**
 * Search query execution condition logic.
 */
public fun interface SearchForQuery {

    /**
     * Search trigger logic. Return `true` to run search operations.
     *
     * @param query query for search operations
     */
    public fun trigger(query: Query): Boolean

    public companion object {

        /** Trigger if the query text is not empty.*/
        public val NotEmpty: SearchForQuery = SearchForQuery { it.query?.isNotEmpty() == true }
    }
}
