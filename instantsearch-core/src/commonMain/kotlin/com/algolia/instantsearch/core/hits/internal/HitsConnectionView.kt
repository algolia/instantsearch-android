package com.algolia.instantsearch.core.hits.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.Presenter
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.core.internal.traceHits
import com.algolia.instantsearch.core.searcher.Searcher

internal data class HitsConnectionView<R, T>(
    private val searcher: Searcher<R>,
    private val view: HitsView<T>,
    private val presenter: Presenter<R, List<T>>
) : ConnectionImpl() {

    init {
        traceHits()
    }

    private val callback: Callback<R?> = { response ->
        if (response != null) {
            view.setHits(presenter(response))
        }
    }

    override fun connect() {
        super.connect()
        searcher.response.subscribe(callback)
    }

    override fun disconnect() {
        super.disconnect()
        searcher.response.unsubscribe(callback)
    }
}
