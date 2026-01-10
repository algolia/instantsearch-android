package com.algolia.instantsearch.stats.internal

import com.algolia.client.model.search.SearchResponse
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.AbstractConnection
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.stats.StatsViewModel

internal data class StatsConnectionSearcher(
    private val viewModel: StatsViewModel,
    private val searcher: Searcher<SearchResponse>,
) : AbstractConnection() {

    private val updateResponse: Callback<SearchResponse?> = { response ->
        viewModel.response.value = response
    }

    override fun connect() {
        super.connect()
        searcher.response.subscribePast(updateResponse)
    }

    override fun disconnect() {
        super.disconnect()
        searcher.response.unsubscribe(updateResponse)
    }
}
