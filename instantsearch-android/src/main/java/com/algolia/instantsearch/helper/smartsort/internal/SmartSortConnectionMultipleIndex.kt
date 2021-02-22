package com.algolia.instantsearch.helper.smartsort.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.smartsort.SmartSortPriority
import com.algolia.instantsearch.core.smartsort.SmartSortViewModel
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.search.model.response.ResponseSearches

/**
 * Connection between smart sort's view model and a multiple index searcher.
 */
internal class SmartSortConnectionMultipleIndex(
    val viewModel: SmartSortViewModel,
    val searcher: SearcherMultipleIndex,
    private val queryIndex: Int,
) : ConnectionImpl() {

    private val priorityCallback: Callback<SmartSortPriority?> = callback@{ priority ->
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
        val dynamicSortPriority = SmartSortPriority.of(receivedRelevancyStrictness)
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
