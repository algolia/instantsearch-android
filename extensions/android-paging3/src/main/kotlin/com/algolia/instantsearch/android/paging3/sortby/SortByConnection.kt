package com.algolia.instantsearch.android.paging3.sortby

import com.algolia.instantsearch.android.paging3.Paginator
import com.algolia.instantsearch.android.paging3.sortby.internal.SortByConnectionPaginator
import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.searcher.IndexNameHolder
import com.algolia.instantsearch.sortby.SortByConnector
import com.algolia.instantsearch.sortby.SortByViewModel

/**
 * Creates a connection between [Paginator] and [SortByViewModel].
 *
 * @param paginator paginator to be connected
 */
public fun <T : Any> SortByViewModel.connectPagedList(paginator: Paginator<T>): Connection {
    return SortByConnectionPaginator(this, paginator)
}

/**
 * Creates a connection between [Paginator] and [SortByConnector].
 *
 * @param paginator paginator to be connected
 */
public fun <S, T> SortByConnector<S>.connectPagedList(
    paginator: Paginator<T>
): Connection where S : Searcher<*>, S : IndexNameHolder, T : Any {
    return viewModel.connectPagedList(paginator)
}
