package com.algolia.instantsearch.tracker.internal

import com.algolia.instantsearch.migration2to3.QueryID

/**
 * A container for query ID.
 */
internal interface QueryIDContainer {

    /**
     * Current query ID.
     */
    public var queryID: QueryID?
}
