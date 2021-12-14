package com.algolia.instantsearch.helper.android.sortby

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.helper.android.sortby.internal.SortByConnectionPagedList
import com.algolia.instantsearch.helper.searcher.IndexNameHolder
import com.algolia.instantsearch.helper.sortby.searcher.SortByConnector
import com.algolia.instantsearch.helper.sortby.searcher.SortByViewModel


public fun <T> SortByViewModel.connectPagedList(pagedList: LiveData<PagedList<T>>): Connection {
    return SortByConnectionPagedList(this, pagedList)
}

public fun <S, T> SortByConnector<S>.connectPagedList(
    pagedList: LiveData<PagedList<T>>
): Connection where S : Searcher<*>, S : IndexNameHolder {
    return viewModel.connectPagedList(pagedList)
}
