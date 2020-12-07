package com.algolia.instantsearch.helper.customdata.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.customdata.QueryRuleCustomDataViewModel
import com.algolia.instantsearch.helper.searcher.SearcherIndex
import com.algolia.search.model.response.ResponseSearch

/**
 * Connection between a rule custom data logic and a single index searcher.
 *
 * @param searcher searcher that handles your searches
 * @param viewModel logic applied to the custom model
 */
internal class QueryRuleCustomDataConnectionSingleIndex<T>(
    val viewModel: QueryRuleCustomDataViewModel<T>,
    val searcher: SearcherIndex<ResponseSearch>,
) : ConnectionImpl() {

    private val callback: Callback<ResponseSearch?> = { response ->
        response?.let { viewModel.extractModel(it) }
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
