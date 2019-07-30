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
import com.algolia.instantsearch.helper.searchbox.SearchMode


public fun <R> SearchBoxViewModel.connectSearcher(
    searcher: Searcher<R>,
    pagedList: List<LiveData<out PagedList<out Any>>>,
    searchAsYouType: SearchMode = SearchMode.AsYouType,
    debouncer: Debouncer = Debouncer(debounceSearchInMillis)
): Connection {
    return SearchBoxConnectionSearcherPagedList(this, searcher, pagedList, searchAsYouType, debouncer)
}

public fun <R> SearchBoxConnectorPagedList<R>.connectView(
    view: SearchBoxView
): Connection {
    return viewModel.connectView(view)
}