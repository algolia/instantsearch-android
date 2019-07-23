package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.core.observable.SubscriptionValue
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.search.client.Index
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.Query
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.*


public class SearcherSingleIndex(
    public var index: Index,
    public val query: Query = Query(),
    public val requestOptions: RequestOptions? = null,
    public val isDisjunctiveFacetingEnabled: Boolean = true,
    override val coroutineScope: CoroutineScope = SearcherScope()
) : Searcher<ResponseSearch> {

    internal val sequencer = Sequencer()

    override val dispatcher: CoroutineDispatcher = defaultDispatcher
    override val isLoading = SubscriptionValue(false)
    override val error = SubscriptionValue<Throwable?>(null)
    override val response = SubscriptionValue<ResponseSearch?>(null)

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        error.value = throwable
    }

    internal var disjunctive: (() -> Pair<List<Attribute>, Set<Filter>>) = { listOf<Attribute>() to setOf() }

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
        val (disjunctiveAttributes, filters) = disjunctive.invoke()

        withContext(dispatcher) { isLoading.value = true }
        val response = if (disjunctiveAttributes.isEmpty() || !isDisjunctiveFacetingEnabled) {
            index.search(query, requestOptions)
        } else {
            index.searchDisjunctiveFacets(query, disjunctiveAttributes, filters)
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