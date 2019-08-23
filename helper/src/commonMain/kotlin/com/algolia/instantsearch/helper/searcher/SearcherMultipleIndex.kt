package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.multipleindex.IndexQuery
import com.algolia.search.model.multipleindex.MultipleQueriesStrategy
import com.algolia.search.model.response.ResponseSearches
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.*


public class SearcherMultipleIndex(
    public val client: ClientSearch,
    public val queries: List<IndexQuery>,
    public val strategy: MultipleQueriesStrategy = MultipleQueriesStrategy.None,
    public val requestOptions: RequestOptions? = null,
    override val coroutineScope: CoroutineScope = SearcherScope()
) : Searcher<ResponseSearches> {

    internal val sequencer = Sequencer()

    override val isLoading = SubscriptionValue(false)
    override val error = SubscriptionValue<Throwable?>(null)
    override val response = SubscriptionValue<ResponseSearches?>(null)

    private val options = requestOptions.withUserAgent()
    private val exceptionHandler = SearcherExceptionHandler(this)

    override fun setQuery(text: String?) {
        queries.forEach { it.query.query = text }
    }

    override fun searchAsync(): Job {
        return coroutineScope.launch(exceptionHandler) {
            isLoading.value = true
            response.value = withContext(Dispatchers.Default) { search() }
            isLoading.value = false
        }.also {
            sequencer.addOperation(it)
        }
    }

    override suspend fun search(): ResponseSearches {
        return client.multipleQueries(queries, strategy, options)
    }

    override fun cancel() {
        sequencer.cancelAll()
    }
}