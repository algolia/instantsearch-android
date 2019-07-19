package com.algolia.instantsearch.helper.loading

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.loading.LoadingViewModel
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.searcher.Searcher


public class LoadingConnectionSearcher<R>(
    val viewModel: LoadingViewModel,
    val searcher: Searcher<R>,
    val debouncer: Debouncer
) : Connection {

    override var isConnected: Boolean = false

    private val eventReload: (Unit) -> Unit = {
        searcher.searchAsync()
    }
    private val updateIsLoading: (Boolean) -> Unit = {
        debouncer.debounce(searcher) {
            viewModel.isLoading.value = it
        }
    }

    override fun connect() {
        super.connect()
        viewModel.isLoading.value = searcher.isLoading.value
        viewModel.eventReload.subscribe(eventReload)
        searcher.isLoading.subscribePast(updateIsLoading)
    }

    override fun disconnect() {
        super.disconnect()
        viewModel.eventReload.unsubscribe(eventReload)
        searcher.isLoading.unsubscribe(updateIsLoading)
    }
}