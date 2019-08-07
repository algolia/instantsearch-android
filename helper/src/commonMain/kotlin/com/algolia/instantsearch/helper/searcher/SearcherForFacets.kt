package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.search.client.Index
import com.algolia.search.model.Attribute
import com.algolia.search.model.response.ResponseSearchForFacets
import com.algolia.search.model.search.Query
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.*


public class SearcherForFacets(
    public var index: Index,
    public val attribute: Attribute,
    public val query: Query = Query(),
    public var facetQuery: String? = null,
    public val requestOptions: RequestOptions? = null,
    override val coroutineScope: CoroutineScope = SearcherScope(),
    override val dispatcher: CoroutineDispatcher = defaultDispatcher
) : Searcher<ResponseSearchForFacets> {

    internal val sequencer = Sequencer()
    override val isLoading = SubscriptionValue(false)
    override val error = SubscriptionValue<Throwable?>(null)
    override val response = SubscriptionValue<ResponseSearchForFacets?>(null)

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        error.value = throwable
    }

    private val options = requestOptions.withUserAgent()

    override fun setQuery(text: String?) {
        facetQuery = text
    }

    override fun searchAsync(): Job {
        return coroutineScope.launch(dispatcher + exceptionHandler) {
            isLoading.value = true
            withContext(Dispatchers.Default) { search() }
            isLoading.value = false
        }.also {
            sequencer.addOperation(it)
        }
    }

    override suspend fun search(): ResponseSearchForFacets {
        withContext(dispatcher) { isLoading.value = true }
        val response = index.searchForFacets(attribute, facetQuery, query, options)
        withContext(dispatcher) {
            this@SearcherForFacets.response.value = response
            isLoading.value = false
        }
        return response
    }

    override fun cancel() {
        sequencer.cancelAll()
    }
}