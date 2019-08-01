package com.algolia.instantsearch.core.loading

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.Callback


internal data class LoadingConnectionView(
    private val viewModel: LoadingViewModel,
    private val view: LoadingView
) : ConnectionImpl() {

    private val updateIsLoading: Callback<Boolean> = { isLoading ->
        view.setIsLoading(isLoading)
    }

    override fun connect() {
        super.connect()
        viewModel.isLoading.subscribePast(updateIsLoading)
        view.onReload = (viewModel.eventReload::send)
    }

    override fun disconnect() {
        super.disconnect()
        viewModel.isLoading.unsubscribe(updateIsLoading)
        view.onReload = null
    }
}