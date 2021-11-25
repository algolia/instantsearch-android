package com.algolia.instantsearch.helper.searcher.hits.internal

import com.algolia.instantsearch.ExperimentalInstantSearch
import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.helper.searcher.SearcherScope
import com.algolia.instantsearch.helper.searcher.hits.HitsSearcher
import com.algolia.instantsearch.helper.searcher.internal.SearcherExceptionHandler
import com.algolia.instantsearch.helper.searcher.internal.withUserAgent
import com.algolia.instantsearch.helper.searcher.multi.internal.MultiSearchComponent
import com.algolia.instantsearch.telemetry.Telemetry
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
internal class DefaultHitsSearcher(
    client: ClientSearch,
    override var indexName: IndexName,
    override val query: Query,
    override val requestOptions: RequestOptions? = null,
    override val isDisjunctiveFacetingEnabled: Boolean = true,
    override val coroutineScope: CoroutineScope = SearcherScope(),
) : HitsSearcher, MultiSearchComponent<IndexQuery, ResponseSearch> {

    private val hitsService = HitsSearchService(client)

    override val isLoading: SubscriptionValue<Boolean> = SubscriptionValue(false)
    override val error: SubscriptionValue<Throwable?> = SubscriptionValue(null)
    override val response: SubscriptionValue<ResponseSearch?> = SubscriptionValue(null)
    override var filterGroups: Set<FilterGroup<*>> by hitsService::filterGroups

    private val exceptionHandler = SearcherExceptionHandler(this)
    private val sequencer = Sequencer()
    private val options = requestOptions.withUserAgent()
    private val indexedQuery: IndexQuery get() = IndexQuery(indexName, query)

    init {
        Telemetry()
    }

    override fun setQuery(text: String?) {
        this.query.query = text
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
        return hitsService.search(HitsSearchService.Request(indexedQuery, isDisjunctiveFacetingEnabled), options)
    }

    override fun collect(): Pair<List<IndexQuery>, (List<ResponseSearch>) -> Unit> {
        return if (isDisjunctiveFacetingEnabled) disjunctiveSearch() else singleQuerySearch()
    }

    private fun disjunctiveSearch(): Pair<List<IndexQuery>, (List<ResponseSearch>) -> Unit> {
        val (queries, disjunctiveFacetCount) = hitsService.advancedQueryOf(indexedQuery)
        return queries to { response.value = hitsService.aggregateResult(it, disjunctiveFacetCount) }
    }

    private fun singleQuerySearch() = listOf(indexedQuery) to ::onSingleQuerySearchResponse

    private fun onSingleQuerySearchResponse(responses: List<ResponseSearch>) {
        response.value = responses.firstOrNull()
    }

    override fun cancel() {
        sequencer.cancelAll()
    }
}
