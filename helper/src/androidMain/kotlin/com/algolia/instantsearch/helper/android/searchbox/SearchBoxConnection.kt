package com.algolia.instantsearch.helper.android.searchbox

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.searchbox.SearchBoxView
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.searchbox.connectionView
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.helper.searchbox.SearchMode


public fun <R> SearchBoxViewModel.connectionSearcher(
    searcher: Searcher<R>,
    pagedList: List<LiveData<out PagedList<out Any>>>,
    searchAsYouType: SearchMode,
    debouncer: Debouncer
): Connection {
    return SearchBoxConnectionSearcherPagedList(this, searcher, pagedList, searchAsYouType, debouncer)
}

public fun <R> SearchBoxWidgetPagedList<R>.connectionView(
    view: SearchBoxView
): Connection {
    return viewModel.connectionView(view)
}