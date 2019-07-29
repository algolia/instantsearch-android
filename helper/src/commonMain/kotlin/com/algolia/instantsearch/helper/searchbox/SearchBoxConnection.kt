package com.algolia.instantsearch.helper.searchbox

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.searchbox.SearchBoxView
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.searchbox.connectView
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.core.searcher.debounceLoadingInMillis
import com.algolia.instantsearch.core.searcher.debounceSearchInMillis


public fun <R> SearchBoxViewModel.connectSearcher(
    searcher: Searcher<R>,
    searchMode: SearchMode = SearchMode.AsYouType,
    debouncer: Debouncer = Debouncer(debounceSearchInMillis)
): Connection {
    return SearchBoxConnectionSearcher(this, searcher, searchMode, debouncer)
}

public fun <R> SearchBoxWidget<R>.connectView(
    view: SearchBoxView
): Connection {
    return viewModel.connectView(view)
}