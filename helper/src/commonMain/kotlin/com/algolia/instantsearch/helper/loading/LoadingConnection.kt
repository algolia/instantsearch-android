@file:JvmName("Loading")

package com.algolia.instantsearch.helper.loading

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.loading.LoadingView
import com.algolia.instantsearch.core.loading.LoadingViewModel
import com.algolia.instantsearch.core.loading.connectView
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.core.searcher.debounceLoadingInMillis
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads


/**
 * Connects this LoadingViewModel to a Searcher, changing the loading state according to its requests.
 */
@JvmOverloads
public fun <R> LoadingViewModel.connectSearcher(
    searcher: Searcher<R>,
    debouncer: Debouncer = Debouncer(debounceLoadingInMillis)
): Connection {
    return LoadingConnectionSearcher(this, searcher, debouncer)
}

/**
 * Connects this LoadingViewModel to a LoadingView, updating it when the loading state changes.
 */
public fun <R> LoadingConnector<R>.connectView(
    view: LoadingView
): Connection {
    return viewModel.connectView(view)
}
