package com.algolia.instantsearch.helper.loading

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.loading.LoadingViewModel
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.core.searcher.debounceLoadingInMillis


public class LoadingConnector<R>(
    public val searcher: Searcher<R>,
    public val viewModel: LoadingViewModel = LoadingViewModel(),
    public val debouncer: Debouncer = Debouncer(debounceLoadingInMillis)
) : ConnectionImpl() {

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