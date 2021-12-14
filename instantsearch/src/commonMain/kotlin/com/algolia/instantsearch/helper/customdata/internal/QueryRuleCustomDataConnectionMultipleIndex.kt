package com.algolia.instantsearch.helper.customdata.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.customdata.QueryRuleCustomDataViewModel
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.search.model.response.ResponseSearches

/**
 * Connection between a rule custom data logic and a single index searcher.
 *
 * @param searcher searcher that handles your searches
 * @param viewModel logic applied to the custom model
 * @param queryIndex index of query from response of which the user data will be extracted
 */
@Deprecated("Use Searchers aggregated with MultiSearcher instead")
internal class QueryRuleCustomDataConnectionMultipleIndex<T>(
    val viewModel: QueryRuleCustomDataViewModel<T>,
    val searcher: SearcherMultipleIndex,
    private val queryIndex: Int,
) : ConnectionImpl() {

    private val callback: Callback<ResponseSearches?> = { responses ->
        responses?.results?.getOrNull(queryIndex)?.let { viewModel.extractModel(it) }
    }

    override fun connect() {
        super.connect()
        searcher.response.subscribe(callback)
    }

    override fun disconnect() {
        super.disconnect()
        searcher.response.unsubscribe(callback)
    }
}
