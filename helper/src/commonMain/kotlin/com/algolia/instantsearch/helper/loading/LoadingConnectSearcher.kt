package com.algolia.instantsearch.helper.loading

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.loading.LoadingViewModel
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.searcher.Searcher


public fun <R> LoadingViewModel.connectionSearcher(
    searcher: Searcher<R>,
    debouncer: Debouncer = Debouncer(200)
): Connection {
    return LoadingConnectionSearcher(this, searcher, debouncer)
}

public fun <R> LoadingViewModel.connectSearcher(
    searcher: Searcher<R>,
    debouncer: Debouncer = Debouncer(200)
): Connection {
    return connectionSearcher(searcher, debouncer).apply { connect() }
}