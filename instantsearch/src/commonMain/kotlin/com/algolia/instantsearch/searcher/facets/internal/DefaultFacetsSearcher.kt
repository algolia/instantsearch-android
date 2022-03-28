package com.algolia.instantsearch.searcher.facets.internal

import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.extension.traceFacetsSearcher
import com.algolia.instantsearch.searcher.SearcherScope
import com.algolia.instantsearch.searcher.facets.FacetsSearcher
import com.algolia.instantsearch.searcher.internal.SearcherExceptionHandler
import com.algolia.instantsearch.searcher.internal.defaultDispatcher
import com.algolia.instantsearch.searcher.internal.runAsLoading
import com.algolia.instantsearch.searcher.internal.withUserAgent
import com.algolia.instantsearch.searcher.multi.internal.MultiSearchComponent
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
    override var facetQuery: String? = null,
    override val requestOptions: RequestOptions? = null,
    override val coroutineScope: CoroutineScope = SearcherScope(),
    override val coroutineDispatcher: CoroutineDispatcher = defaultDispatcher,
) : FacetsSearcher, MultiSearchComponent<FacetIndexQuery, ResponseSearchForFacets> {

    override val isLoading: SubscriptionValue<Boolean> = SubscriptionValue(false)
    override val error: SubscriptionValue<Throwable?> = SubscriptionValue(null)
    override val response: SubscriptionValue<ResponseSearchForFacets?> = SubscriptionValue(null)

    private val options get() = requestOptions.withUserAgent()
    private val exceptionHandler = SearcherExceptionHandler(this)
    private val sequencer = Sequencer()
    private val indexedQuery get() = FacetIndexQuery(indexName, query, attribute, facetQuery)

    init {
        traceFacetsSearcher()
    }

    override fun collect(): Pair<List<FacetIndexQuery>, (List<ResponseSearchForFacets>) -> Unit> {
        return listOf(indexedQuery) to { response.value = it.firstOrNull() }
    }

    override fun setQuery(text: String?) {
        facetQuery = text
    }

    override fun searchAsync(): Job {
        return coroutineScope.launch(exceptionHandler) {
            isLoading.runAsLoading(::search)
        }.also {
            sequencer.addOperation(it)
        }
    }

    override suspend fun search(): ResponseSearchForFacets = withContext(coroutineDispatcher) {
        searchService.search(indexedQuery, options)
    }

    override fun cancel() {
        sequencer.cancelAll()
    }
}
