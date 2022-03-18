package com.algolia.instantsearch.helper.android.sortby

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.helper.android.sortby.internal.SortByConnectionPagedList
import com.algolia.instantsearch.helper.searcher.IndexNameHolder
import com.algolia.instantsearch.helper.sortby.SortByConnector
import com.algolia.instantsearch.helper.sortby.SortByViewModel

@Deprecated("use Paginator from InstantSearch Android Paging3 extension package instead")
public fun <T> SortByViewModel.connectPagedList(pagedList: LiveData<PagedList<T>>): Connection {
    return SortByConnectionPagedList(this, pagedList)
}

@Deprecated("use Paginator from InstantSearch Android Paging3 extension package instead")
public fun <S, T> SortByConnector<S>.connectPagedList(
    pagedList: LiveData<PagedList<T>>
): Connection where S : Searcher<*>, S : IndexNameHolder {
    return viewModel.connectPagedList(pagedList)
}
