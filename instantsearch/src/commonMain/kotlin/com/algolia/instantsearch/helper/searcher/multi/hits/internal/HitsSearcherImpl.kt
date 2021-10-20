package com.algolia.instantsearch.helper.searcher.multi.hits.internal

import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.helper.searcher.SearcherScope
import com.algolia.instantsearch.helper.searcher.internal.SearcherExceptionHandler
import com.algolia.instantsearch.helper.searcher.internal.withUserAgent
import com.algolia.instantsearch.helper.searcher.multi.hits.HitsSearcher
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.filter.FilterGroup
import com.algolia.search.model.multipleindex.IndexQuery
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * The component handling search requests and managing the search sessions.
 * This implementation searches a single index.
 */
internal class HitsSearcherImpl(
    client: ClientSearch,
    override val indexedQuery: IndexQuery,
    requestOptions: RequestOptions? = null,
    override val coroutineScope: CoroutineScope = SearcherScope(),
) : HitsSearcher {

    override val isLoading: SubscriptionValue<Boolean> = SubscriptionValue(false)
    override val error: SubscriptionValue<Throwable?> = SubscriptionValue(null)
    override val response: SubscriptionValue<ResponseSearch?> = SubscriptionValue(null)

    private val exceptionHandler = SearcherExceptionHandler(this)
    private val hitsService = HitsService(client)
    private val sequencer = Sequencer()
    private val options = requestOptions.withUserAgent()

    internal var filterGroups: Set<FilterGroup<*>> = setOf()

    override fun setQuery(text: String?) {
        indexedQuery.query.query = text
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

    override suspend fun search(): ResponseSearch {
        return hitsService.search(HitsService.Request(indexedQuery, filterGroups), options)
    }

    override fun cancel() {
        sequencer.cancelAll()
    }
}
