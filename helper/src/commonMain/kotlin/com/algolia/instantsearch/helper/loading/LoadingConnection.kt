package com.algolia.instantsearch.helper.loading

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.loading.LoadingView
import com.algolia.instantsearch.core.loading.LoadingViewModel
import com.algolia.instantsearch.core.loading.connectionView
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.searcher.Searcher


public fun <R> LoadingViewModel.connectionSearcher(
    searcher: Searcher<R>,
    debouncer: Debouncer
): Connection {
    return LoadingConnectionSearcher(this, searcher, debouncer)
}

public fun <R> LoadingWidget<R>.connectionView(
    view: LoadingView
): Connection {
    return viewModel.connectionView(view)
}
