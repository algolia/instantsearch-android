package com.algolia.instantsearch.helper.searcher.multi.facets.internal

import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.helper.searcher.SearcherScope
import com.algolia.instantsearch.helper.searcher.internal.SearcherExceptionHandler
import com.algolia.instantsearch.helper.searcher.internal.withUserAgent
import com.algolia.instantsearch.helper.searcher.multi.facets.FacetsSearcher
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.multipleindex.FacetIndexQuery
import com.algolia.search.model.response.ResponseSearchForFacets
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
internal class FacetsSearcherImpl(
    client: ClientSearch,
    override val indexedQuery: FacetIndexQuery,
    requestOptions: RequestOptions? = null,
    override val coroutineScope: CoroutineScope = SearcherScope(),
) : FacetsSearcher {

    override val isLoading: SubscriptionValue<Boolean> = SubscriptionValue(false)
    override val error: SubscriptionValue<Throwable?> = SubscriptionValue(null)
    override val response: SubscriptionValue<ResponseSearchForFacets?> = SubscriptionValue(null)

    private val service = FacetsService(client)
    private val options = requestOptions.withUserAgent()
    private val exceptionHandler = SearcherExceptionHandler(this)
    private val sequencer = Sequencer()

    override fun setQuery(text: String?) {
        indexedQuery.query.query = text
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
