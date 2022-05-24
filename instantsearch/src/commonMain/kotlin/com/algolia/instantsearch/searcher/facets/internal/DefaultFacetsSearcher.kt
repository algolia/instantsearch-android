package com.algolia.instantsearch.searcher.facets.internal

import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.extension.traceFacetsSearcher
import com.algolia.instantsearch.searcher.SearcherScope
import com.algolia.instantsearch.searcher.facets.FacetsSearcher
import com.algolia.instantsearch.searcher.facets.SearchForFacetQuery
import com.algolia.instantsearch.searcher.internal.SearcherExceptionHandler
import com.algolia.instantsearch.searcher.internal.defaultDispatcher
import com.algolia.instantsearch.searcher.internal.runAsLoading
import com.algolia.instantsearch.searcher.internal.withUserAgent
import com.algolia.instantsearch.searcher.multi.internal.MultiSearchComponent
import com.algolia.instantsearch.searcher.multi.internal.MultiSearchOperation
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.multipleindex.FacetIndexQuery
import com.algolia.search.model.response.ResponseSearchForFacets
import com.algolia.search.model.search.Query
import com.algolia.search.transport.RequestOptions
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
    override var indexName: IndexName,
    override val query: Query,
    override val attribute: Attribute,
    override var facetQuery: String?,
    override val requestOptions: RequestOptions?,
    override val coroutineScope: CoroutineScope,
    override val coroutineDispatcher: CoroutineDispatcher,
    private val triggerSearchFor: SearchForFacetQuery
) : FacetsSearcher, MultiSearchComponent<FacetIndexQuery, ResponseSearchForFacets> {

    override val isLoading: SubscriptionValue<Boolean> = SubscriptionValue(false)
    override val error: SubscriptionValue<Throwable?> = SubscriptionValue(null)
    override val response: SubscriptionValue<ResponseSearchForFacets?> = SubscriptionValue(null)

    private val exceptionHandler = SearcherExceptionHandler(this)
    private val sequencer = Sequencer()

    private val options get() = requestOptions.withUserAgent()
    private val indexedQuery get() = FacetIndexQuery(indexName, query, attribute, facetQuery)

    init {
        traceFacetsSearcher()
    }

    override fun collect(): MultiSearchOperation<FacetIndexQuery, ResponseSearchForFacets> {
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

    override suspend fun search(): ResponseSearchForFacets? {
        if (!triggerSearchFor.trigger(query, attribute, facetQuery)) return null
        return withContext(coroutineDispatcher) {
            searchService.search(indexedQuery, options)
        }
    }

    override fun cancel() {
        sequencer.cancelAll()
    }
}
