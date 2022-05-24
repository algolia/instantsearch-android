package com.algolia.instantsearch.searcher.hits.internal

import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.extension.traceHitsSearcher
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.searcher.hits.SearchForQuery
import com.algolia.instantsearch.searcher.internal.SearcherExceptionHandler
import com.algolia.instantsearch.searcher.internal.runAsLoading
import com.algolia.instantsearch.searcher.internal.withUserAgent
import com.algolia.instantsearch.searcher.multi.internal.MultiSearchComponent
import com.algolia.instantsearch.searcher.multi.internal.MultiSearchOperation
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.FilterGroup
import com.algolia.search.model.multipleindex.IndexQuery
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.Query
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * The component handling search requests and managing the search sessions.
 * This implementation searches a single index.
 */
internal class DefaultHitsSearcher(
    private val searchService: HitsSearchService,
    override var indexName: IndexName,
    override val query: Query,
    override val requestOptions: RequestOptions?,
    override val isDisjunctiveFacetingEnabled: Boolean,
    override val coroutineScope: CoroutineScope,
    override val coroutineDispatcher: CoroutineDispatcher,
    private val triggerSearchFor: SearchForQuery
) : HitsSearcher, MultiSearchComponent<IndexQuery, ResponseSearch> {

    override val isLoading: SubscriptionValue<Boolean> = SubscriptionValue(false)
    override val error: SubscriptionValue<Throwable?> = SubscriptionValue(null)
    override val response: SubscriptionValue<ResponseSearch?> = SubscriptionValue(null)
    override var filterGroups: Set<FilterGroup<*>> by searchService::filterGroups

    private val exceptionHandler = SearcherExceptionHandler(this)
    private val sequencer = Sequencer()

    private val options get() = requestOptions.withUserAgent()
    private val indexedQuery get() = IndexQuery(indexName, query)

    init {
        traceHitsSearcher()
    }

    override fun setQuery(text: String?) {
        this.query.query = text
    }

    override fun searchAsync(): Job {
        return coroutineScope.launch(exceptionHandler) {
            isLoading.runAsLoading {
                response.value = search()
            }
        }.also {
            sequencer.addOperation(it)
        }
    }

    override suspend fun search(): ResponseSearch? {
        if (triggerSearchFor.trigger(query)) return null
        return withContext(coroutineDispatcher) {
            searchService.search(HitsSearchService.Request(indexedQuery, isDisjunctiveFacetingEnabled), options)
        }
    }

    override fun collect(): MultiSearchOperation<IndexQuery, ResponseSearch> {
        return if (isDisjunctiveFacetingEnabled) disjunctiveSearch() else singleQuerySearch()
    }


    private fun disjunctiveSearch(): MultiSearchOperation<IndexQuery, ResponseSearch> {
        val (queries, disjunctiveFacetCount) = searchService.advancedQueryOf(indexedQuery)
        return MultiSearchOperation(
            requests = queries,
            completion = { response.value = searchService.aggregateResult(it, disjunctiveFacetCount) },
            shouldTrigger = triggerSearchFor.trigger(query)
        )
    }

    private fun singleQuerySearch(): MultiSearchOperation<IndexQuery, ResponseSearch> {
        return MultiSearchOperation(
            requests = listOf(indexedQuery),
            completion = ::onSingleQuerySearchResponse,
            shouldTrigger = triggerSearchFor.trigger(query)
        )
    }

    private fun onSingleQuerySearchResponse(responses: List<ResponseSearch>) {
        response.value = responses.firstOrNull()
    }

    override fun cancel() {
        sequencer.cancelAll()
    }
}
