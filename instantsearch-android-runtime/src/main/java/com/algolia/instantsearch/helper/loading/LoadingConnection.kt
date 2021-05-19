package com.algolia.instantsearch.helper.loading

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.loading.LoadingView
import com.algolia.instantsearch.core.loading.LoadingViewModel
import com.algolia.instantsearch.core.loading.connectView
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.core.searcher.debounceLoadingInMillis
import com.algolia.instantsearch.helper.loading.internal.LoadingConnectionSearcher

public fun <R> LoadingViewModel.connectSearcher(
    searcher: Searcher<R>,
    debouncer: Debouncer = Debouncer(debounceLoadingInMillis),
): Connection {
    return LoadingConnectionSearcher(this, searcher, debouncer)
}

/**
 * Create a connection between a view and the loading components.
 *
 * @param view the concrete view displayed during loading
 */
public fun <R> LoadingConnector<R>.connectView(
    view: LoadingView,
): Connection {
    return viewModel.connectView(view)
}
