package com.algolia.instantsearch.compose.filter.state

import com.algolia.instantsearch.compose.filter.state.internal.FilterStateConnectionPagingData
import com.algolia.instantsearch.compose.list.Paginator
import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.helper.filter.state.FilterState

/**
 * Create a connection between a [Paginator] and [FilterState].
 *
 * @param paginator paginator to be connected
 */
public fun <T : Any> FilterState.connectPaginator(paginator: Paginator<T>): Connection {
    return FilterStateConnectionPagingData(paginator, this)
}
