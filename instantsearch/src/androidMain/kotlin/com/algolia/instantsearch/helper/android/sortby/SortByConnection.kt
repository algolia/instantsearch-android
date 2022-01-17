package com.algolia.instantsearch.helper.android.sortby

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.helper.android.list.Paginator
import com.algolia.instantsearch.helper.android.sortby.internal.SortByConnectionPagedList
import com.algolia.instantsearch.helper.android.sortby.internal.SortByConnectionPaginator
import com.algolia.instantsearch.helper.searcher.IndexNameHolder
import com.algolia.instantsearch.helper.sortby.SortByConnector
import com.algolia.instantsearch.helper.sortby.SortByViewModel

@Deprecated("PagedList is deperecated, use Paginator instead")
public fun <T : Any> SortByViewModel.connectPagedList(pagedList: LiveData<PagedList<T>>): Connection {
    return SortByConnectionPagedList(this, pagedList)
}

@Deprecated("PagedList is deperecated, use Paginator instead")
public fun <S, T> SortByConnector<S>.connectPagedList(
    pagedList: LiveData<PagedList<T>>
): Connection where S : Searcher<*>, S : IndexNameHolder, T : Any {
    return viewModel.connectPagedList(pagedList)
}

/**
 * Create a connection between a [Paginator] and [SortByConnector].
 *
 * @param paginator paginator to be connected
 */
public fun <S, T> SortByConnector<S>.connectPaginator(
    paginator: Paginator<T>
): Connection where S : Searcher<*>, S : IndexNameHolder, T : Any {
    return viewModel.connectPaginator(paginator)
}

/**
 * Create a connection between a [Paginator] and [SortByViewModel].
 *
 * @param paginator paginator to be connected
 */
public fun <T : Any> SortByViewModel.connectPaginator(paginator: Paginator<T>): Connection {
    return SortByConnectionPaginator(this, paginator)
}
