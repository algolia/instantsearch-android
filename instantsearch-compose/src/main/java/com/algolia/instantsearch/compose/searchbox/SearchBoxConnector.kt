package com.algolia.instantsearch.compose.searchbox

import com.algolia.instantsearch.compose.list.Paginator
import com.algolia.instantsearch.compose.searchbox.internal.SearchBoxConnectionPaginator
import com.algolia.instantsearch.core.ExperimentalInstantSearch
import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.helper.searchbox.SearchBoxConnector
import com.algolia.instantsearch.helper.searchbox.SearchMode

/**
 * Create a connection between [Paginator] and [SearchBoxConnector].
 *
 * @param paginator paginator to be connected
 */
@ExperimentalInstantSearch
public fun <R, T : Any> SearchBoxConnector<R>.connectPaginator(paginator: Paginator<T>): Connection {
    return viewModel.connectPaginator(paginator, searchMode)
}

/**
 * Create a connection between [Paginator] and [SearchBoxViewModel].
 *
 * @param paginator paginator to be connected
 */
@ExperimentalInstantSearch
public fun <T : Any> SearchBoxViewModel.connectPaginator(
    paginator: Paginator<T>,
    searchMode: SearchMode = SearchMode.AsYouType
): Connection {
    return SearchBoxConnectionPaginator(this, paginator, searchMode)
}
