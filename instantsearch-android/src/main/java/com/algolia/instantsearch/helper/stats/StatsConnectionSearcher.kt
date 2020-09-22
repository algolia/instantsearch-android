package com.algolia.instantsearch.helper.stats

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.response.ResponseSearch

internal data class StatsConnectionSearcher(
    private val viewModel: StatsViewModel,
    private val searcher: SearcherSingleIndex,
) : ConnectionImpl() {

    private val updateResponse: Callback<ResponseSearch?> = { response ->
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
