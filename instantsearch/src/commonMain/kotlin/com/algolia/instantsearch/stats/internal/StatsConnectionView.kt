package com.algolia.instantsearch.stats.internal

import com.algolia.client.model.search.SearchResponse
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.AbstractConnection
import com.algolia.instantsearch.stats.StatsPresenter
import com.algolia.instantsearch.stats.StatsView
import com.algolia.instantsearch.stats.StatsViewModel

internal data class StatsConnectionView<T>(
    private val viewModel: StatsViewModel,
    private val view: StatsView<T>,
    private val presenter: StatsPresenter<T>,
) : AbstractConnection() {

    private val updateText: Callback<SearchResponse?> = { response ->
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
