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

        /**
         * Trigger search for all queries.
         */
        public val All: SearchForQuery = SearchForQuery { true }

        /**
         * Trigger if the query length is greater or equals to [length].
         *
         * @param length minimal query length
         */
        public fun lengthAtLeast(length: Int): SearchForQuery = SearchForQuery { (it.query?.length ?: 0) >= length }
    }
}
