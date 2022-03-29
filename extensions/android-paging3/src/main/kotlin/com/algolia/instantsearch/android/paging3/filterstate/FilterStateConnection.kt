package com.algolia.instantsearch.android.paging3.filterstate

import com.algolia.instantsearch.android.paging3.Paginator
import com.algolia.instantsearch.android.paging3.filterstate.internal.FilterStateConnectionPaginator
import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.filter.state.FilterState

/**
 * Create a connection between a [Paginator] and [FilterState].
 *
 * @param paginator paginator to be connected
 */
public fun <T : Any> FilterState.connectPaginator(paginator: Paginator<T>): Connection {
    return FilterStateConnectionPaginator(paginator, this)
}
