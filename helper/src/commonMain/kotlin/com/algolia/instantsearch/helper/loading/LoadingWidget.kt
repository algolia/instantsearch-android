package com.algolia.instantsearch.helper.loading

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.connection.safeConnect
import com.algolia.instantsearch.core.connection.safeDisconnect
import com.algolia.instantsearch.core.loading.LoadingView
import com.algolia.instantsearch.core.loading.LoadingViewModel
import com.algolia.instantsearch.core.loading.connectView
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.searcher.Searcher


public class LoadingWidget<R>(
    public val view: LoadingView,
    public val searcher: Searcher<R>,
    public val viewModel: LoadingViewModel = LoadingViewModel(),
    public val debouncer: Debouncer = Debouncer(200)
) : ConnectionImpl() {

    private val connections = listOf(
        viewModel.connectView(view, false),
        viewModel.connectSearcher(searcher, debouncer, false)
    )

    override fun connect() {
        super.connect()
        connections.forEach { it.safeConnect() }
    }

    override fun disconnect() {
        super.disconnect()
        connections.forEach { it.safeDisconnect() }
    }
}