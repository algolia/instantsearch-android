package com.algolia.instantsearch.helper.dynamic.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.dynamic.DynamicSortPriority
import com.algolia.instantsearch.core.dynamic.DynamicSortViewModel
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.response.ResponseSearch

/**
 * Connection between dynamic sort's view model and a single index searcher.
 */
internal class DynamicSortToggleConnectionSingleIndex(
    val viewModel: DynamicSortViewModel,
    val searcher: SearcherSingleIndex,
) : ConnectionImpl() {

    private val priorityCallback: Callback<DynamicSortPriority?> = callback@{ priority ->
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
        val dynamicSortPriority = DynamicSortPriority.of(receivedRelevancyStrictness)
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
