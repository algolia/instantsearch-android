package com.algolia.instantsearch.helper.android.searchbox

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.searchbox.SearchBoxView
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.searchbox.connectView
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.core.searcher.debounceSearchInMillis
import com.algolia.instantsearch.helper.android.list.Paginator
import com.algolia.instantsearch.helper.android.searchbox.internal.SearchBoxConnectionPaginator
import com.algolia.instantsearch.helper.android.searchbox.internal.SearchBoxConnectionSearcherPagedList
import com.algolia.instantsearch.helper.searchbox.SearchBoxConnector
import com.algolia.instantsearch.helper.searchbox.SearchMode

public fun <R> SearchBoxViewModel.connectSearcher(
    searcher: Searcher<R>,
    pagedList: List<LiveData<out PagedList<out Any>>>,
    searchAsYouType: SearchMode = SearchMode.AsYouType,
    debouncer: Debouncer = Debouncer(debounceSearchInMillis),
): Connection {
    return SearchBoxConnectionSearcherPagedList(this, searcher, pagedList, searchAsYouType, debouncer)
}

public fun <R> SearchBoxConnectorPagedList<R>.connectView(
    view: SearchBoxView,
): Connection {
    return viewModel.connectView(view)
}

/**
 * Create a connection between [Paginator] and [SearchBoxConnector].
 *
 * @param paginator component handling paged data
 */
public fun <R, T : Any> SearchBoxConnector<R>.connectPaginator(paginator: Paginator<T>): Connection {
    return viewModel.connectPaginator(paginator, searchMode)
}

/**
 * Create a connection between [Paginator] and [SearchBoxViewModel].
 *
 * @param paginator component handling paged data
 */
public fun <T : Any> SearchBoxViewModel.connectPaginator(
    paginator: Paginator<T>,
    searchMode: SearchMode = SearchMode.AsYouType
): Connection {
    return SearchBoxConnectionPaginator(this, paginator, searchMode)
}
