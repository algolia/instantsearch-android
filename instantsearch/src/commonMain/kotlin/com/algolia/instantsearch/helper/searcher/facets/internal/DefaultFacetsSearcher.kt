package com.algolia.instantsearch.helper.searcher.facets.internal

import com.algolia.instantsearch.ExperimentalInstantSearch
import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.helper.extension.traceFacetsSearcher
import com.algolia.instantsearch.helper.searcher.SearcherScope
import com.algolia.instantsearch.helper.searcher.facets.FacetsSearcher
import com.algolia.instantsearch.helper.searcher.internal.SearcherExceptionHandler
import com.algolia.instantsearch.helper.searcher.internal.withUserAgent
import com.algolia.instantsearch.helper.searcher.multi.internal.MultiSearchComponent
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.multipleindex.FacetIndexQuery
import com.algolia.search.model.response.ResponseSearchForFacets
import com.algolia.search.model.search.Query
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * The component handling search requests and managing the search sessions.
 * This implementation searches for facet values.
 */
internal class DefaultFacetsSearcher(
    client: ClientSearch,
    override var indexName: IndexName,
    override val query: Query,
    override val attribute: Attribute,
    override var facetQuery: String? = null,
    override val requestOptions: RequestOptions? = null,
    override val coroutineScope: CoroutineScope = SearcherScope(),
) : FacetsSearcher, MultiSearchComponent<FacetIndexQuery, ResponseSearchForFacets> {

    override val isLoading: SubscriptionValue<Boolean> = SubscriptionValue(false)
    override val error: SubscriptionValue<Throwable?> = SubscriptionValue(null)
    override val response: SubscriptionValue<ResponseSearchForFacets?> = SubscriptionValue(null)

    private val service = FacetsSearchService(client)
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
            isLoading.value = true
            response.value = withContext(Dispatchers.Default) { search() }
            isLoading.value = false
        }.also {
            sequencer.addOperation(it)
        }
    }

    override suspend fun search(): ResponseSearchForFacets {
        return service.search(indexedQuery, options)
    }

    override fun cancel() {
        sequencer.cancelAll()
    }
}
