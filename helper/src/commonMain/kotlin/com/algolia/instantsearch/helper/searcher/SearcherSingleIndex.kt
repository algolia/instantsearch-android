package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.search.client.Index
import com.algolia.search.model.filter.FilterGroup
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.Query
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.*


public class SearcherSingleIndex(
    public var index: Index,
    public val query: Query = Query(),
    public val requestOptions: RequestOptions? = null,
    public val isDisjunctiveFacetingEnabled: Boolean = true,
    override val coroutineScope: CoroutineScope = SearcherScope(),
    override val dispatcher: CoroutineDispatcher = defaultDispatcher
) : Searcher<ResponseSearch> {

    internal val sequencer = Sequencer()

    override val isLoading = SubscriptionValue(false)
    override val error = SubscriptionValue<Throwable?>(null)
    override val response = SubscriptionValue<ResponseSearch?>(null)


    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        error.value = throwable
    }

    private val options = requestOptions.withUserAgent()

    internal var filterGroups: Set<FilterGroup<*>> = setOf()

    override fun setQuery(text: String?) {
        this.query.query = text
    }

    override fun searchAsync(): Job {
        return coroutineScope.launch(dispatcher + exceptionHandler) {
            withContext(Dispatchers.Default) { search() }
        }.also {
            sequencer.addOperation(it)
        }
    }

    override suspend fun search(): ResponseSearch {
        withContext(dispatcher) { isLoading.value = true }
        val response = if (isDisjunctiveFacetingEnabled) {
            index.advancedSearch(query, filterGroups, options)
        } else {
            index.search(query, options)
        }
        withContext(dispatcher) {
            this@SearcherSingleIndex.response.value = response
            isLoading.value = false
        }
        return response
    }

    override fun cancel() {
        sequencer.cancelAll()
    }
}