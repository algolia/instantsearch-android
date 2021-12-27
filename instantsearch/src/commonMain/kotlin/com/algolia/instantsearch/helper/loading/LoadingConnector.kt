package com.algolia.instantsearch.helper.loading

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.loading.LoadingViewModel
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.core.searcher.debounceLoadingInMillis
import com.algolia.instantsearch.helper.extension.traceLoadingConnector

/**
 * Components that show a loading indicator during pending requests.
 *
 * @param searcher the Searcher that handles your searches.
 * @param viewModel the logic applied to the loading indicator.
 * @param debouncer delays searcher operations by a specified time duration.
 */
public data class LoadingConnector<R>(
    public val searcher: Searcher<R>,
    public val viewModel: LoadingViewModel = LoadingViewModel(),
    public val debouncer: Debouncer = Debouncer(debounceLoadingInMillis),
) : ConnectionImpl() {

    init {
        traceLoadingConnector()
    }

    private val connectionSearcher = viewModel.connectSearcher(searcher, debouncer)

    override fun connect() {
        super.connect()
        connectionSearcher.connect()
    }

    override fun disconnect() {
        super.disconnect()
        connectionSearcher.disconnect()
    }
}
