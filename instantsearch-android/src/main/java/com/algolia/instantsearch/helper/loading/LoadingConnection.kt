package com.algolia.instantsearch.helper.loading

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.loading.LoadingView
import com.algolia.instantsearch.core.loading.LoadingViewModel
import com.algolia.instantsearch.core.loading.connectView
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.core.searcher.debounceLoadingInMillis

public fun <R> LoadingViewModel.connectSearcher(
    searcher: Searcher<R>,
    debouncer: Debouncer = Debouncer(debounceLoadingInMillis),
): Connection {
    return LoadingConnectionSearcher(this, searcher, debouncer)
}

public fun <R> LoadingConnector<R>.connectView(
    view: LoadingView,
): Connection {
    return viewModel.connectView(view)
}
