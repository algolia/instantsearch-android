package com.algolia.instantsearch.helper.tracker

import com.algolia.search.model.QueryID

/**
 * A container for query ID.
 */
public interface QueryIDContainer {

    /**
     * Current query ID.
     */
    public var queryID: QueryID?
}
