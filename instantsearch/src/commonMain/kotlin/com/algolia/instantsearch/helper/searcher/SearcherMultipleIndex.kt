package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.helper.extension.traceMultiSearcher
import com.algolia.instantsearch.helper.searcher.internal.SearcherExceptionHandler
import com.algolia.instantsearch.helper.searcher.internal.withUserAgent
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.multipleindex.IndexQuery
import com.algolia.search.model.multipleindex.MultipleQueriesStrategy
import com.algolia.search.model.response.ResponseSearches
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * The component handling search requests and managing the search sessions.
 * This implementation searches in multiple indices.
 * This is useful for a federated search, or query suggestions search experience.
 */
public class SearcherMultipleIndex(
    public val client: ClientSearch,
    public val queries: List<IndexQuery>,
    public val strategy: MultipleQueriesStrategy = MultipleQueriesStrategy.None,
    public val requestOptions: RequestOptions? = null,
    override val coroutineScope: CoroutineScope = SearcherScope(),
) : Searcher<ResponseSearches> {

    internal val sequencer = Sequencer()

    override val isLoading: SubscriptionValue<Boolean> = SubscriptionValue(false)
    override val error: SubscriptionValue<Throwable?> = SubscriptionValue(null)
    override val response: SubscriptionValue<ResponseSearches?> = SubscriptionValue(null)

    private val options = requestOptions.withUserAgent()
    private val exceptionHandler = SearcherExceptionHandler(this)

    init {
        traceMultiSearcher()
    }

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
