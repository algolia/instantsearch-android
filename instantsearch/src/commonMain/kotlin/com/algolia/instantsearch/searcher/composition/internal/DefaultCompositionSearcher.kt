package com.algolia.instantsearch.searcher.composition.internal

import com.algolia.client.model.search.SearchParamsObject
import com.algolia.client.model.search.SearchResponse
import com.algolia.client.transport.RequestOptions
import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.filter.FilterGroup
import com.algolia.instantsearch.searcher.composition.CompositionSearcher
import com.algolia.instantsearch.searcher.hits.SearchForQuery
import com.algolia.instantsearch.searcher.internal.SearcherExceptionHandler
import com.algolia.instantsearch.searcher.internal.withAlgoliaAgent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * The component handling search requests and managing the search sessions.
 * This implementation runs an Algolia Composition.
 */
internal class DefaultCompositionSearcher(
    private val searchService: CompositionSearchService,
    override var compositionID: String,
    override var query: SearchParamsObject,
    override val requestOptions: RequestOptions?,
    override val coroutineScope: CoroutineScope,
    override val coroutineDispatcher: CoroutineDispatcher,
    private val triggerSearchFor: SearchForQuery,
) : CompositionSearcher {

    override val isLoading: SubscriptionValue<Boolean> = SubscriptionValue(false)
    override val error: SubscriptionValue<Throwable?> = SubscriptionValue(null)
    override val response: SubscriptionValue<SearchResponse?> = SubscriptionValue(null)
    override var filterGroups: Set<FilterGroup<*>> by searchService::filterGroups

    private val exceptionHandler = SearcherExceptionHandler(this)
    private val sequencer = Sequencer()

    private val options get() = requestOptions.withAlgoliaAgent()

    override fun setQuery(text: String?) {
        query = query.copy(query = text)
    }

    override fun searchAsync(): Job {
        return coroutineScope.launch(exceptionHandler) {
            isLoading.value = true
            try {
                response.value = search()
            } finally {
                // Also runs on cancellation, which never reaches the
                // CoroutineExceptionHandler: the flag can't get stuck to true.
                isLoading.value = false
            }
        }.also {
            sequencer.addOperation(it)
        }
    }

    override suspend fun search(): SearchResponse? {
        if (!triggerSearchFor.trigger(query)) return null
        return withContext(coroutineDispatcher) {
            searchService.search(CompositionSearchService.Request(compositionID, query), options)
        }
    }

    override fun cancel() {
        sequencer.cancelAll()
    }
}
