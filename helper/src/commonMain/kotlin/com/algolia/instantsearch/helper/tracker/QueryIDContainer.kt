package com.algolia.instantsearch.helper.tracker

import com.algolia.search.model.QueryID

/**
 * Container for a Query ID.
 */
internal interface QueryIDContainer {

    public var queryID: QueryID?
}
