package com.algolia.instantsearch.core.loading.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.AbstractConnection
import com.algolia.instantsearch.core.loading.LoadingView
import com.algolia.instantsearch.core.loading.LoadingViewModel

internal data class LoadingConnectionView(
    private val viewModel: LoadingViewModel,
    private val view: LoadingView
) : AbstractConnection() {

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
