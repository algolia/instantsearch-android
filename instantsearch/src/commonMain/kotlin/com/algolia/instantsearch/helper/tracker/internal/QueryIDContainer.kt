package com.algolia.instantsearch.helper.tracker.internal

import com.algolia.search.model.QueryID

/**
 * A container for query ID.
 */
internal interface QueryIDContainer {

    /**
     * Current query ID.
     */
    public var queryID: QueryID?
}
