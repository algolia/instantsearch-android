package com.algolia.instantsearch.searcher.hits.internal

import com.algolia.client.api.InsightsClient
import com.algolia.client.model.search.SearchParamsObject
import com.algolia.client.model.search.SearchResponse
import com.algolia.client.transport.RequestOptions
import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.extension.traceHitsSearcher
import com.algolia.instantsearch.filter.FilterGroup
import com.algolia.instantsearch.searcher.multi.internal.types.IndexQuery
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.searcher.hits.SearchForQuery
import com.algolia.instantsearch.searcher.internal.SearcherExceptionHandler
import com.algolia.instantsearch.searcher.internal.withAlgoliaAgent
import com.algolia.instantsearch.searcher.multi.internal.MultiSearchComponent
import com.algolia.instantsearch.searcher.multi.internal.MultiSearchOperation
import kotlinx.coroutines.*

/**
 * The component handling search requests and managing the search sessions.
 * This implementation searches a single index.
 */
internal class DefaultHitsSearcher(
    private val searchService: HitsSearchService,
    private val insights: InsightsClient,
    override var indexName: String,
    override val query: SearchParamsObject,
    override val requestOptions: RequestOptions?,
    override val isDisjunctiveFacetingEnabled: Boolean,
    override val coroutineScope: CoroutineScope,
    override val coroutineDispatcher: CoroutineDispatcher,
    private val triggerSearchFor: SearchForQuery,
    override var isAutoSendingHitsViewEvents: Boolean,
    override var userToken: String,
) : HitsSearcher, MultiSearchComponent<IndexQuery, SearchResponse> {

    override val isLoading: SubscriptionValue<Boolean> = SubscriptionValue(false)
    override val error: SubscriptionValue<Throwable?> = SubscriptionValue(null)
    override val response: SubscriptionValue<SearchResponse?> = SubscriptionValue(null)
    override var filterGroups: Set<FilterGroup<*>> by searchService::filterGroups

    private val exceptionHandler = SearcherExceptionHandler(this)
    private val sequencer = Sequencer()

    // Mutable query for setQuery() - note: query is val in interface, so we use a private var
    private var mutableQuery: SearchParamsObject = query

    private val options get() = requestOptions.withAlgoliaAgent()
    private val indexedQuery get() = IndexQuery(indexName, mutableQuery)

    private val maxObjectIDsPerEvent = 10

    init {
        traceHitsSearcher()
    }

    override fun setQuery(text: String?) {
        mutableQuery = mutableQuery.copy(query = text)
    }

    override fun searchAsync(): Job {
        return coroutineScope.launch(exceptionHandler) {
            isLoading.value = true
            val responseSearch = search()
            response.value = responseSearch
            isLoading.value = false
            sendInsightsEvents(responseSearch)
        }.also {
            sequencer.addOperation(it)
        }
    }

    override suspend fun search(): SearchResponse? {
        if (!triggerSearchFor.trigger(mutableQuery)) return null
        return withContext(coroutineDispatcher) {
            searchService.search(HitsSearchService.Request(indexedQuery, isDisjunctiveFacetingEnabled), options)
        }
    }

    private suspend fun sendInsightsEvents(response: SearchResponse?) {
        // TODO: Implement v3 insights events sending
        // For now, this is disabled as InsightsEvent was removed from migration2to3
        // Need to use v3 EventsItems API instead
    }

    override fun collect(): MultiSearchOperation<IndexQuery, SearchResponse> {
        return if (isDisjunctiveFacetingEnabled) disjunctiveSearch() else singleQuerySearch()
    }


    private fun disjunctiveSearch(): MultiSearchOperation<IndexQuery, SearchResponse> {
        val (queries, disjunctiveFacetCount) = searchService.advancedQueryOf(indexedQuery)
        return MultiSearchOperation(
            requests = queries,
            completion = { response.value = searchService.aggregateResult(it, disjunctiveFacetCount) },
            shouldTrigger = triggerSearchFor.trigger(mutableQuery)
        )
    }

    private fun singleQuerySearch(): MultiSearchOperation<IndexQuery, SearchResponse> {
        return MultiSearchOperation(
            requests = listOf(indexedQuery),
            completion = ::onSingleQuerySearchResponse,
            shouldTrigger = triggerSearchFor.trigger(mutableQuery)
        )
    }

    private fun onSingleQuerySearchResponse(responses: List<SearchResponse>) {
        val responseSearch = responses.firstOrNull()
        response.value = responseSearch
        coroutineScope.launch {
            sendInsightsEvents(responseSearch)
        }
    }

    override fun cancel() {
        sequencer.cancelAll()
    }

    companion object {
        private val ViewEventObjects = "View Objects"
    }
}
