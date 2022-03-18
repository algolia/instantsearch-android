package com.algolia.instantsearch.customdata.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.customdata.QueryRuleCustomDataViewModel
import com.algolia.instantsearch.searcher.SearcherForHits
import com.algolia.search.model.response.ResponseSearch

/**
 * Connection between a rule custom data logic and a single index searcher.
 *
 * @param searcher searcher that handles your searches
 * @param viewModel logic applied to the custom model
 */
internal class QueryRuleCustomDataConnectionSearcherForHits<T>(
    val viewModel: QueryRuleCustomDataViewModel<T>,
    val searcher: SearcherForHits<*>,
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
