package com.algolia.instantsearch.relevantsort.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.AbstractConnection
import com.algolia.instantsearch.core.relevantsort.RelevantSortPriority
import com.algolia.instantsearch.core.relevantsort.RelevantSortViewModel
import com.algolia.instantsearch.migration2to3.Query
import com.algolia.instantsearch.migration2to3.ResponseSearch
import com.algolia.instantsearch.searcher.SearcherForHits

/**
 * Connection between relevant sort's view model and a single index searcher.
 */
internal class RelevantSortConnectionSearcherForHits(
    val viewModel: RelevantSortViewModel,
    val searcher: SearcherForHits<Query>,
) : AbstractConnection() {

    private val priorityCallback: Callback<RelevantSortPriority?> = callback@{ priority ->
        if (priority == null) return@callback
        searcher.query.relevancyStrictness = priority.relevancyStrictness
        searcher.searchAsync()
    }

    private val responseCallback: Callback<ResponseSearch?> = callback@{ response ->
        if (response == null) return@callback
        val receivedRelevancyStrictness = response.appliedRelevancyStrictnessOrNull ?: run {
            viewModel.priority.value = null
            return@callback
        }
        val dynamicSortPriority = RelevantSortPriority.of(receivedRelevancyStrictness)
        if (dynamicSortPriority != viewModel.priority.value) {
            viewModel.priority.value = dynamicSortPriority
        }
    }

    override fun connect() {
        super.connect()
        viewModel.priority.subscribe(priorityCallback)
        searcher.response.subscribePast(responseCallback)
    }

    override fun disconnect() {
        super.disconnect()
        viewModel.priority.unsubscribe(priorityCallback)
        searcher.response.unsubscribe(responseCallback)
    }
}
