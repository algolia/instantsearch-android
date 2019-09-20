package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.search.client.ClientPlaces
import com.algolia.search.model.places.PlacesQuery
import com.algolia.search.model.response.ResponseSearchPlacesMono
import com.algolia.search.model.search.Language
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.*
import kotlin.jvm.JvmField


public class SearcherPlaces(
    @JvmField
    val client: ClientPlaces = ClientPlaces(),
    @JvmField
    val language: Language = Language.English,
    @JvmField
    val query: PlacesQuery = PlacesQuery(),
    @JvmField
    val requestOptions: RequestOptions? = null,
    override val coroutineScope: CoroutineScope = SearcherScope()
) : Searcher<ResponseSearchPlacesMono> {

    private val sequencer = Sequencer()

    override val isLoading = SubscriptionValue(false)
    override val error = SubscriptionValue<Throwable?>(null)
    override val response = SubscriptionValue<ResponseSearchPlacesMono?>(null)

    private val exceptionHandler = SearcherExceptionHandler(this)

    override fun setQuery(text: String?) {
        query.query = text
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

    override suspend fun search(): ResponseSearchPlacesMono {
        return client.searchPlaces(language, query, requestOptions)
    }

    override fun cancel() {
        sequencer.cancelAll()
    }
}