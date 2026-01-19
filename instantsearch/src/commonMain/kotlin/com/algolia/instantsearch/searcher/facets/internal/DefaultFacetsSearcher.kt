package com.algolia.instantsearch.searcher.facets.internal

import com.algolia.client.model.search.SearchForFacetValuesResponse
import com.algolia.client.model.search.SearchParamsObject
import com.algolia.client.transport.RequestOptions
import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.extension.traceFacetsSearcher
import com.algolia.instantsearch.searcher.multi.internal.types.FacetIndexQuery
import com.algolia.instantsearch.searcher.facets.FacetsSearcher
import com.algolia.instantsearch.searcher.facets.SearchForFacetQuery
import com.algolia.instantsearch.searcher.internal.SearcherExceptionHandler
import com.algolia.instantsearch.searcher.internal.runAsLoading
import com.algolia.instantsearch.searcher.internal.withAlgoliaAgent
import com.algolia.instantsearch.searcher.multi.internal.MultiSearchComponent
import com.algolia.instantsearch.searcher.multi.internal.MultiSearchOperation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * The component handling search requests and managing the search sessions.
 * This implementation searches for facet values.
 */
internal class DefaultFacetsSearcher(
    private val searchService: FacetsSearchService,
    override var indexName: String,
    override val query: SearchParamsObject,
    override val attribute: String,
    override var facetQuery: String?,
    override val requestOptions: RequestOptions?,
    override val coroutineScope: CoroutineScope,
    override val coroutineDispatcher: CoroutineDispatcher,
    private val triggerSearchFor: SearchForFacetQuery
) : FacetsSearcher, MultiSearchComponent<FacetIndexQuery, SearchForFacetValuesResponse> {

    override val isLoading: SubscriptionValue<Boolean> = SubscriptionValue(false)
    override val error: SubscriptionValue<Throwable?> = SubscriptionValue(null)
    override val response: SubscriptionValue<SearchForFacetValuesResponse?> = SubscriptionValue(null)

    private val exceptionHandler = SearcherExceptionHandler(this)
    private val sequencer = Sequencer()

    private val options get() = requestOptions.withAlgoliaAgent()
    private val indexedQuery get() = FacetIndexQuery(indexName, query, attribute, facetQuery)

    init {
        traceFacetsSearcher()
    }

    override fun collect(): MultiSearchOperation<FacetIndexQuery, SearchForFacetValuesResponse> {
        return MultiSearchOperation(
            requests = listOf(indexedQuery),
            completion = { response.value = it.firstOrNull() },
            shouldTrigger = triggerSearchFor.trigger(query, attribute, facetQuery)
        )
    }

    override fun setQuery(text: String?) {
        facetQuery = text
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

    override suspend fun search(): SearchForFacetValuesResponse? {
        if (!triggerSearchFor.trigger(query, attribute, facetQuery)) return null
        return withContext(coroutineDispatcher) {
            searchService.search(indexedQuery, options)
        }
    }

    override fun cancel() {
        sequencer.cancelAll()
    }
}
