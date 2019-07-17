package com.algolia.instantsearch.helper.loading

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.loading.LoadingConnectionView
import com.algolia.instantsearch.core.loading.LoadingView
import com.algolia.instantsearch.core.loading.LoadingViewModel
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.searcher.Searcher


public class LoadingWidget<R>(
    public val view: LoadingView,
    public val searcher: Searcher<R>,
    public val viewModel: LoadingViewModel = LoadingViewModel(),
    public val debouncer: Debouncer = Debouncer(200)
) : Connection {

    override var isConnected: Boolean = false

    private val connectionView = LoadingConnectionView(viewModel, view)
    private val connectionSearcher = LoadingConnectionSearcher(viewModel, searcher, debouncer)

    override fun connect() {
        super.connect()
        connectionView.connect()
        connectionSearcher.connect()
    }

    override fun disconnect() {
        super.disconnect()
        connectionView.disconnect()
        connectionSearcher.disconnect()
    }
}