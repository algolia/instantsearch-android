package com.algolia.instantsearch.core.loading

import com.algolia.instantsearch.core.connection.Connection


public class LoadingConnectionView(
    val viewModel: LoadingViewModel,
    val view: LoadingView
) : Connection {

    override var isConnected: Boolean = false

    private val updateView: (Boolean) -> Unit = {
        view.setItem(it)
    }

    override fun connect() {
        super.connect()
        viewModel.isLoading.subscribePast(updateView)
        view.onClick = (viewModel.event::send)
    }

    override fun disconnect() {
        super.disconnect()
        viewModel.isLoading.unsubscribe(updateView)
        view.onClick = null
    }
}