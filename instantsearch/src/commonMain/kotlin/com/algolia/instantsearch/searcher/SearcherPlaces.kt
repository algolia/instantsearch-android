package com.algolia.instantsearch.searcher

import com.algolia.client.transport.RequestOptions
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.searcher.internal.SearcherExceptionHandler
import com.algolia.instantsearch.searcher.internal.defaultDispatcher
import com.algolia.instantsearch.searcher.internal.runAsLoading
import com.algolia.search.client.ClientPlaces
import com.algolia.search.model.places.PlacesQuery
import com.algolia.search.model.response.ResponseSearchPlacesMono
import com.algolia.search.model.search.Language
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * The component handling search requests and managing the search sessions.
 * This implementation searches for places.
 */
@Deprecated("Places feature is deprecated")
public class SearcherPlaces(
    public val client: ClientPlaces = ClientPlaces(),
    public val language: com.algolia.search.model.search.Language = com.algolia.search.model.search.Language.English,
    public val query: PlacesQuery = PlacesQuery(),
    public val requestOptions: RequestOptions? = null,
    override val coroutineScope: CoroutineScope = SearcherScope(),
    override val coroutineDispatcher: CoroutineDispatcher = defaultDispatcher,
) : Searcher<ResponseSearchPlacesMono> {

    private val sequencer = Sequencer()

    override val isLoading: SubscriptionValue<Boolean> = SubscriptionValue(false)
    override val error: SubscriptionValue<Throwable?> = SubscriptionValue(null)
    override val response: SubscriptionValue<ResponseSearchPlacesMono?> = SubscriptionValue(null)

    private val exceptionHandler = SearcherExceptionHandler(this)

    override fun setQuery(text: String?) {
        query.query = text
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

    override suspend fun search(): ResponseSearchPlacesMono = withContext(coroutineDispatcher) {
        throw UnsupportedOperationException(
            "Places is deprecated and not supported with the Kotlin v3 API client."
        )
    }

    override fun cancel() {
        sequencer.cancelAll()
    }
}
