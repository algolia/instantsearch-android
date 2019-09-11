package com.algolia.instantsearch.core.loading

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.CallbackImpl
import com.algolia.instantsearch.core.CallbackUnit
import com.algolia.instantsearch.core.connection.ConnectionImpl


internal data class LoadingConnectionView(
    private val viewModel: LoadingViewModel,
    private val view: LoadingView
) : ConnectionImpl() {

    private val updateIsLoading: Callback<Boolean> = CallbackImpl { isLoading ->
        view.setIsLoading(isLoading)
    }

    override fun connect() {
        super.connect()
        viewModel.isLoading.subscribePast(updateIsLoading)
        view.onReload = CallbackUnit { viewModel.eventReload.send(Unit) }
    }

    override fun disconnect() {
        super.disconnect()
        viewModel.isLoading.unsubscribe(updateIsLoading)
        view.onReload = null
    }
}