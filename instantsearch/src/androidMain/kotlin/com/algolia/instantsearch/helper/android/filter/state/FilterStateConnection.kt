package com.algolia.instantsearch.helper.android.filter.state

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.helper.android.filter.state.internal.FilterStateConnectionPagedList
import com.algolia.instantsearch.helper.android.filter.state.internal.FilterStateConnectionPaginator
import com.algolia.instantsearch.helper.android.list.Paginator
import com.algolia.instantsearch.helper.filter.state.FilterState

@Deprecated("PagedList is deperecated, use Paginator instead")
public fun <T : Any> FilterState.connectPagedList(pagedList: LiveData<PagedList<T>>): Connection {
    return FilterStateConnectionPagedList(pagedList, this)
}

/**
 * Create a connection between a [Paginator] and [FilterState].
 *
 * @param paginator paginator to be connected
 */
public fun <T : Any> FilterState.connectPaginator(paginator: Paginator<T>): Connection {
    return FilterStateConnectionPaginator(paginator, this)
}
