package com.algolia.instantsearch.compose.filter.facet

import com.algolia.instantsearch.compose.filter.facet.internal.FacetListConnectionPager
import com.algolia.instantsearch.compose.list.Paginator
import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.helper.filter.facet.FacetListConnector
import com.algolia.instantsearch.helper.filter.facet.FacetListViewModel

/**
 * Create a connection between [Paginator] and [FacetListConnector].
 *
 * @param paginator paginator to be connected
 */
public fun <T : Any> FacetListConnector.connectPaginator(paginator: Paginator<T>): Connection {
    return viewModel.connectPaginator(paginator)
}

/**
 * Create a connection between [Paginator] and [FacetListViewModel].
 *
 * @param paginator paginator to be connected
 */
public fun <T : Any> FacetListViewModel.connectPaginator(paginator: Paginator<T>): Connection {
    return FacetListConnectionPager(this, paginator)
}
