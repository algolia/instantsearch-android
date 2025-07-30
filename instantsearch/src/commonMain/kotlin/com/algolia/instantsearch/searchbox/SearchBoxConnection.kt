package com.algolia.instantsearch.searchbox

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.searchbox.SearchBoxView
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.searchbox.connectView
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.core.searcher.debounceSearchInMillis
import com.algolia.instantsearch.searchbox.internal.SearchBoxConnectionSearcher

public fun <R> SearchBoxViewModel.connectSearcher(
    searcher: Searcher<R>,
    searchMode: SearchMode = SearchMode.AsYouType,
    debouncer: Debouncer = Debouncer(debounceSearchInMillis),
    searchOnQueryUpdate: Boolean = true,
): Connection {
    return SearchBoxConnectionSearcher(this, searcher, searchMode, debouncer, searchOnQueryUpdate)
}

/**
 * Create a connection between a view and the searchbox.
 *
 * @param view the view that handles the input
 */
public fun <R> SearchBoxConnector<R>.connectView(
    view: SearchBoxView,
): Connection {
    return viewModel.connectView(view)
}
