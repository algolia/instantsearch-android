package com.algolia.instantsearch.compose.sortby

import com.algolia.instantsearch.compose.list.Paginator
import com.algolia.instantsearch.compose.sortby.internal.SortByConnectionPaginator
import com.algolia.instantsearch.core.ExperimentalInstantSearch
import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.helper.sortby.SortByConnector
import com.algolia.instantsearch.helper.sortby.SortByViewModel

/**
 * Creates a connection between [Paginator] and [SortByViewModel].
 *
 * @param paginator paginator to be connected
 */
@ExperimentalInstantSearch
public fun <T : Any> SortByViewModel.connectPagedList(paginator: Paginator<T>): Connection {
    return SortByConnectionPaginator(this, paginator)
}

/**
 * Creates a connection between [Paginator] and [SortByConnector].
 *
 * @param paginator paginator to be connected
 */
@ExperimentalInstantSearch
public fun <T : Any> SortByConnector.connectPagedList(paginator: Paginator<T>): Connection {
    return viewModel.connectPagedList(paginator)
}
