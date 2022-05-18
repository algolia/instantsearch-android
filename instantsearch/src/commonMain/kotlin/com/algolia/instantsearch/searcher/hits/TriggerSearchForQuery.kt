package com.algolia.instantsearch.searcher.hits

import com.algolia.search.model.search.Query

/**
 * Search query execution condition logic.
 */
public fun interface TriggerSearchForQuery {

    /**
     * Search trigger logic. Return `true` to run search operations.
     *
     * @param query query for search operations
     */
    public fun trigger(query: Query): Boolean
}
