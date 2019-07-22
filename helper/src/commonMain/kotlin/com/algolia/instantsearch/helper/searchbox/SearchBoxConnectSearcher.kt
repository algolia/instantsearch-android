package com.algolia.instantsearch.helper.searchbox

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.autoConnect
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.searcher.Searcher


public fun <R> SearchBoxViewModel.connectSearcher(
    searcher: Searcher<R>,
    searchAsYouType: Boolean = true,
    debouncer: Debouncer = Debouncer(100),
    connect: Boolean = true
): Connection {
    return SearchBoxConnectionSearcher(this, searcher, searchAsYouType, debouncer).autoConnect(connect)
}