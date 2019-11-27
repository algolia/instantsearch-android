package com.algolia.instantsearch.helper.android.sortby

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.helper.sortby.SortByConnector
import com.algolia.instantsearch.helper.sortby.SortByViewModel


public fun <T> SortByViewModel.connectPagedList(pagedList: LiveData<PagedList<T>>): Connection {
    return SortByConnectionPagedList(this, pagedList)
}

public fun <T> SortByConnector.connectPagedList(pagedList: LiveData<PagedList<T>>): Connection {
    return SortByConnectionPagedList(viewModel, pagedList)
}