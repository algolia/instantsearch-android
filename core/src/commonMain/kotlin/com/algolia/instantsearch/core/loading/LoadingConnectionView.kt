package com.algolia.instantsearch.core.loading

import com.algolia.instantsearch.core.connection.Connection


public class LoadingConnectionView(
    public val viewModel: LoadingViewModel,
    public val view: LoadingView
) : Connection {

    override var isConnected: Boolean = false

    private val updateIsLoading: (Boolean) -> Unit = { isLoading ->
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