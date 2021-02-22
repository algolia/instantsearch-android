package com.algolia.instantsearch.helper.smartsort.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.smartsort.SmartSortPriority
import com.algolia.instantsearch.core.smartsort.SmartSortViewModel
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.response.ResponseSearch

/**
 * Connection between smart sort's view model and a single index searcher.
 */
internal class SmartSortConnectionSingleIndex(
    val viewModel: SmartSortViewModel,
    val searcher: SearcherSingleIndex,
) : ConnectionImpl() {

    private val priorityCallback: Callback<SmartSortPriority?> = callback@{ priority ->
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
