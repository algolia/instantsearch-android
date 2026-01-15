package com.algolia.instantsearch.searcher.hits.internal

import com.algolia.client.api.InsightsClient
import com.algolia.client.model.search.SearchParamsObject
import com.algolia.client.model.search.SearchResponse
import com.algolia.client.transport.RequestOptions
import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.extension.traceHitsSearcher
import com.algolia.instantsearch.migration2to3.EventName
import com.algolia.instantsearch.migration2to3.FilterGroup
import com.algolia.instantsearch.migration2to3.IndexQuery
import com.algolia.instantsearch.migration2to3.InsightsEvent
import com.algolia.instantsearch.migration2to3.ObjectID
import com.algolia.instantsearch.migration2to3.ResponseSearch
import com.algolia.instantsearch.migration2to3.UserToken
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
    override var userToken: UserToken,
) : HitsSearcher, MultiSearchComponent<IndexQuery, SearchResponse> {

    override val isLoading: SubscriptionValue<Boolean> = SubscriptionValue(false)
    override val error: SubscriptionValue<Throwable?> = SubscriptionValue(null)
    override val response: SubscriptionValue<ResponseSearch?> = SubscriptionValue(null)
    override var filterGroups: Set<FilterGroup<*>> by searchService::filterGroups

    private val exceptionHandler = SearcherExceptionHandler(this)
    private val sequencer = Sequencer()

    private val options get() = requestOptions.withAlgoliaAgent()
    private val indexedQuery get() = IndexQuery(indexName, query)

    private val maxObjectIDsPerEvent = 10

    init {
        traceHitsSearcher()
    }

    override fun setQuery(text: String?) {
        this.query.query = text
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

    override suspend fun search(): ResponseSearch? {
        if (!triggerSearchFor.trigger(query)) return null
        return withContext(coroutineDispatcher) {
            searchService.search(HitsSearchService.Request(indexedQuery, isDisjunctiveFacetingEnabled), options)
        }
    }

    private suspend fun sendInsightsEvents(response: ResponseSearch?) {
        if (response != null && isAutoSendingHitsViewEvents) {
            val events = getViewEvents(response)
            if (events.isNotEmpty()) {
                insights.customPost(events)
            }
        }
    }
    private fun getViewEvents(response: SearchResponse): List<InsightsEvent.View> {
        return response.hits
            ?.map { hit -> hit.objectID }
            ?.chunked(maxObjectIDsPerEvent)
            ?.map {
                InsightsEvent.View(
                    eventName = "Hits Viewed",
                    indexName = indexName,
                    userToken = userToken,
                    resources = InsightsEvent.Resources.ObjectIDs(it),
                )
            } ?: listOf()
    }

    override fun collect(): MultiSearchOperation<IndexQuery, SearchResponse> {
        return if (isDisjunctiveFacetingEnabled) disjunctiveSearch() else singleQuerySearch()
    }


    private fun disjunctiveSearch(): MultiSearchOperation<IndexQuery, SearchResponse> {
        val (queries, disjunctiveFacetCount) = searchService.advancedQueryOf(indexedQuery)
        return MultiSearchOperation(
            requests = queries,
            completion = { response.value = searchService.aggregateResult(it, disjunctiveFacetCount) },
            shouldTrigger = triggerSearchFor.trigger(query)
        )
    }

    private fun singleQuerySearch(): MultiSearchOperation<IndexQuery, SearchResponse> {
        return MultiSearchOperation(
            requests = listOf(indexedQuery),
            completion = ::onSingleQuerySearchResponse,
            shouldTrigger = triggerSearchFor.trigger(query)
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
