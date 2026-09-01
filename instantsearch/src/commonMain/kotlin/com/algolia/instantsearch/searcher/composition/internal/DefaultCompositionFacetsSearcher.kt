package com.algolia.instantsearch.searcher.composition.internal

import com.algolia.client.model.search.SearchForFacetValuesResponse
import com.algolia.client.model.search.SearchParamsObject
import com.algolia.client.transport.RequestOptions
import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.searcher.composition.CompositionFacetsSearcher
import com.algolia.instantsearch.searcher.facets.SearchForFacetQuery
import com.algolia.instantsearch.searcher.internal.SearcherExceptionHandler
import com.algolia.instantsearch.searcher.internal.withAlgoliaAgent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * The component handling search requests and managing the search sessions.
 * This implementation searches for facet values using the Algolia Composition API.
 */
internal class DefaultCompositionFacetsSearcher(
    private val searchService: CompositionFacetsSearchService,
    override var compositionID: String,
    override var query: SearchParamsObject,
    override val attribute: String,
    override var facetQuery: String?,
    override var maxFacetHits: Int?,
    override val requestOptions: RequestOptions?,
    override val coroutineScope: CoroutineScope,
    override val coroutineDispatcher: CoroutineDispatcher,
    private val triggerSearchFor: SearchForFacetQuery,
) : CompositionFacetsSearcher {

    override val isLoading: SubscriptionValue<Boolean> = SubscriptionValue(false)
    override val error: SubscriptionValue<Throwable?> = SubscriptionValue(null)
    override val response: SubscriptionValue<SearchForFacetValuesResponse?> = SubscriptionValue(null)

    private val exceptionHandler = SearcherExceptionHandler(this)
    private val sequencer = Sequencer()

    private val options get() = requestOptions.withAlgoliaAgent()
    private val request
        get() = CompositionFacetsSearchService.Request(compositionID, query, attribute, facetQuery, maxFacetHits)

    override fun setQuery(text: String?) {
        facetQuery = text
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

    override suspend fun search(): SearchForFacetValuesResponse? {
        if (!triggerSearchFor.trigger(query, attribute, facetQuery)) return null
        return withContext(coroutineDispatcher) {
            searchService.search(request, options)
        }
    }

    override fun cancel() {
        sequencer.cancelAll()
    }
}
