package com.algolia.instantsearch.helper.relevantsort.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.relevantsort.RelevantSortPriority
import com.algolia.instantsearch.core.relevantsort.RelevantSortViewModel
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.search.model.response.ResponseSearches

/**
 * Connection between relevant sort's view model and a multiple index searcher.
 */
@Deprecated("Use Searchers aggregated with MultiSearcher instead")
internal class RelevantSortConnectionMultipleIndex(
    val viewModel: RelevantSortViewModel,
    val searcher: SearcherMultipleIndex,
    private val queryIndex: Int,
) : ConnectionImpl() {

    private val priorityCallback: Callback<RelevantSortPriority?> = callback@{ priority ->
        if (priority == null) return@callback
        searcher.queries[queryIndex].query.relevancyStrictness = priority.relevancyStrictness
        searcher.searchAsync()
    }

    private val responseCallback: Callback<ResponseSearches?> = callback@{ responses ->
        if (responses == null) return@callback
        val receivedRelevancyStrictness =
            responses.results[queryIndex].appliedRelevancyStrictnessOrNull ?: run {
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
