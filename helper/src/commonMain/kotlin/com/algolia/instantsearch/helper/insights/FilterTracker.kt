package com.algolia.instantsearch.helper.insights

import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.helper.searcher.SearcherScope
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.client.ClientInsights
import com.algolia.search.endpoint.EndpointInsightsUser
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.insights.EventName
import com.algolia.search.model.insights.UserToken
import io.ktor.client.response.HttpResponse
import kotlinx.coroutines.*


public class FilterTracker(
    private val searcher: SearcherSingleIndex,
    private val clientInsights: ClientInsights,
    private val userToken: UserToken,
    private val coroutineScope: CoroutineScope = SearcherScope()
) : EndpointInsightsUser by clientInsights.User(userToken) {

    val error = SubscriptionValue<Throwable?>(null)
    val response = SubscriptionValue<HttpResponse?>(null)

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        error.value = throwable
    }

    public fun clickedFilter(eventName: EventName, filters: List<Filter.Facet>) {
        coroutineScope.launch(exceptionHandler) {
            response.value = withContext(Dispatchers.Default) {
                clickedFilters(searcher.index.indexName, eventName, filters)
            }
        }
    }


    public fun viewedFilters(eventName: EventName, filters: List<Filter.Facet>) {
        coroutineScope.launch(exceptionHandler) {
            response.value = withContext(Dispatchers.Default) {
                viewedFilters(searcher.index.indexName, eventName, filters)
            }
        }
    }

    public fun convertedFilters(eventName: EventName, filters: List<Filter.Facet>) {
        coroutineScope.launch(exceptionHandler) {
            response.value = withContext(Dispatchers.Default) {
                convertedFilters(searcher.index.indexName, eventName, filters)
            }
        }
    }

}