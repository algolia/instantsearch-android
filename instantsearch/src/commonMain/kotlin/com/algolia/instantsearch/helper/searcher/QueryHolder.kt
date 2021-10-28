package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.core.ExperimentalInstantSearch
import com.algolia.search.model.search.Query

/**
 * Component holding a query.
 */
@ExperimentalInstantSearch
public interface QueryHolder {

    /**
     * Query for search operations.
     */
    public val query: Query
}
