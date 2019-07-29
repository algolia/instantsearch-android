package com.algolia.instantsearch.helper.loading

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.loading.LoadingView
import com.algolia.instantsearch.core.loading.LoadingViewModel
import com.algolia.instantsearch.core.loading.connectView
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.searcher.Searcher


public fun <R> LoadingViewModel.connectSearcher(
    searcher: Searcher<R>,
    debouncer: Debouncer
): Connection {
    return LoadingConnectionSearcher(this, searcher, debouncer)
}

public fun <R> LoadingWidget<R>.connectView(
    view: LoadingView
): Connection {
    return viewModel.connectView(view)
}
