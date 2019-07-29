package com.algolia.instantsearch.helper.searchbox

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.searchbox.SearchBoxView
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.searchbox.connectView
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.searcher.Searcher


public fun <R> SearchBoxViewModel.connectSearcher(
    searcher: Searcher<R>,
    searchMode: SearchMode,
    debouncer: Debouncer
): Connection {
    return SearchBoxConnectionSearcher(this, searcher, searchMode, debouncer)
}

public fun <R> SearchBoxWidget<R>.connectView(
    view: SearchBoxView
): Connection {
    return viewModel.connectView(view)
}