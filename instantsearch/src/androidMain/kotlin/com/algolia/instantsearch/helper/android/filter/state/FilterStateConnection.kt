package com.algolia.instantsearch.helper.android.filter.state

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.helper.android.filter.state.internal.FilterStateConnectionPagedList
import com.algolia.instantsearch.helper.filter.state.FilterState

@Deprecated("use Paginator from InstantSearch Android Paging3 extension package instead")
public fun <T> FilterState.connectPagedList(pagedList: LiveData<PagedList<T>>): Connection {
    return FilterStateConnectionPagedList(pagedList, this)
}
