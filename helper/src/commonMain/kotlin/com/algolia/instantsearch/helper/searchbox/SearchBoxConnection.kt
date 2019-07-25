package com.algolia.instantsearch.helper.searchbox

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.searchbox.SearchBoxView
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.searchbox.connectionView
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.searcher.Searcher


public fun <R> SearchBoxViewModel.connectionSearcher(
    searcher: Searcher<R>,
    searchMode: SearchMode,
    debouncer: Debouncer
): Connection {
    return SearchBoxConnectionSearcher(this, searcher, searchMode, debouncer)
}

public fun <R> SearchBoxWidget<R>.connectionView(
    view: SearchBoxView
): Connection {
    return viewModel.connectionView(view)
}