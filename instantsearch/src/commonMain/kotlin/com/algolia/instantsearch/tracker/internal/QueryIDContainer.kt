package com.algolia.instantsearch.tracker.internal

/**
 * A container for query ID.
 */
internal interface QueryIDContainer {

    /**
     * Current query ID.
     */
    public var queryID: String?
}
