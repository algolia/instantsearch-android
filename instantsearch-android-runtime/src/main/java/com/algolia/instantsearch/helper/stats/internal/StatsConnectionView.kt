package com.algolia.instantsearch.helper.stats.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.stats.StatsPresenter
import com.algolia.instantsearch.helper.stats.StatsView
import com.algolia.instantsearch.helper.stats.StatsViewModel
import com.algolia.search.model.response.ResponseSearch

internal data class StatsConnectionView<T>(
    private val viewModel: StatsViewModel,
    private val view: StatsView<T>,
    private val presenter: StatsPresenter<T>,
) : ConnectionImpl() {

    private val updateText: Callback<ResponseSearch?> = { response ->
        view.setText(presenter(response))
    }

    override fun connect() {
        super.connect()
        viewModel.response.subscribePast(updateText)
    }

    override fun disconnect() {
        super.disconnect()
        viewModel.response.unsubscribe(updateText)
    }
}
