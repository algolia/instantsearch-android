package com.algolia.instantsearch.helper.searcher

import com.algolia.search.model.params.CommonSearchParameters

/**
 * Component holding a query.
 */
public interface QueryHolder<T : CommonSearchParameters> {

    /**
     * Query for search operations.
     */
    public val query: T
}
