package com.algolia.instantsearch.helper.searcher.multi.hits.internal

import com.algolia.instantsearch.core.ExperimentalInstantSearch
import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.helper.searcher.SearcherScope
import com.algolia.instantsearch.helper.searcher.internal.SearcherExceptionHandler
import com.algolia.instantsearch.helper.searcher.internal.withUserAgent
import com.algolia.instantsearch.helper.searcher.multi.hits.HitsSearcher
import com.algolia.instantsearch.helper.searcher.multi.internal.MultiSearchComponent
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.FilterGroup
import com.algolia.search.model.multipleindex.IndexQuery
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.Query
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
@ExperimentalInstantSearch
internal class HitsSearcherImpl(
    client: ClientSearch,
    override var indexName: IndexName,
    override val query: Query,
    override val requestOptions: RequestOptions? = null,
    override val coroutineScope: CoroutineScope = SearcherScope(),
) : HitsSearcher, MultiSearchComponent<IndexQuery, ResponseSearch> {

    override val indexedQuery: IndexQuery get() = IndexQuery(indexName, query)
    override val isLoading: SubscriptionValue<Boolean> = SubscriptionValue(false)
    override val error: SubscriptionValue<Throwable?> = SubscriptionValue(null)
    override val response: SubscriptionValue<ResponseSearch?> = SubscriptionValue(null)

    private val exceptionHandler = SearcherExceptionHandler(this)
    private val hitsService = HitsSearchService(client)
    private val sequencer = Sequencer()
    private val options = requestOptions.withUserAgent()

    override var filterGroups: Set<FilterGroup<*>> = setOf()

    override fun setQuery(text: String?) {
        this.query.query = text
    }

    override fun collect(): Pair<List<IndexQuery>, (List<ResponseSearch>) -> Unit> {
        val (queries, disjunctiveFacetCount) = hitsService.advancedQueryOf(indexedQuery, filterGroups)
        return queries to { response.value = hitsService.aggregateResult(it, disjunctiveFacetCount) }
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
        return hitsService.search(HitsSearchService.Request(indexedQuery, filterGroups), options)
    }

    override fun cancel() {
        sequencer.cancelAll()
    }
}
