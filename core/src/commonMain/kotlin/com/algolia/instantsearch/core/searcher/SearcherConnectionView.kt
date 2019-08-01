package com.algolia.instantsearch.core.searcher

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl


internal data class SearcherConnectionView<T, R>(
    private val searcher: Searcher<R>,
    private val view: (T) -> Unit,
    private val transform: (R?) -> T
) : ConnectionImpl() {

    private val subscription: Callback<R?> = { responses ->
        view(transform(responses))
    }

    override fun connect() {
        super.connect()
        searcher.response.subscribe(subscription)
    }

    override fun disconnect() {
        super.disconnect()
        searcher.response.unsubscribe(subscription)
    }
}