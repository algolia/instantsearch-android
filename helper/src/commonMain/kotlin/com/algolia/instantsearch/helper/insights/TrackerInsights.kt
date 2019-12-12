package com.algolia.instantsearch.helper.insights

import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.helper.searcher.SearcherScope
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.client.ClientInsights
import com.algolia.search.client.ClientSearch
import com.algolia.search.endpoint.EndpointInsightsUser
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.algolia.search.model.ObjectID
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.insights.EventName
import com.algolia.search.model.insights.UserToken
import io.ktor.client.response.HttpResponse
import kotlinx.coroutines.*


fun stuff() {
    val appID = ApplicationID("")
    val apiKey = APIKey("")
    val client = ClientSearch(appID, apiKey)
    val indexName = IndexName("blabla")
    val index = client.initIndex(indexName)
    val searcher = SearcherSingleIndex(index)
    val insights = ClientInsights(appID, apiKey)
    val tracker = TrackerInsights(searcher, insights, UserToken("hello"))

    tracker.view()
}

public class TrackerInsights(
    val searcher: SearcherSingleIndex,
    val clientInsights: ClientInsights,
    val userToken: UserToken,
    val coroutineScope: CoroutineScope = SearcherScope()
) : EndpointInsightsUser by clientInsights.User(userToken) {

    val error = SubscriptionValue<Throwable?>(null)
    val response = SubscriptionValue<HttpResponse?>(null)

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        searcher.error.value = throwable
    }

    fun clickedObjectIDs(eventName: EventName, objectIDs: List<ObjectID>) {
        coroutineScope.launch(exceptionHandler) {
            response.value = withContext(Dispatchers.Default) {
                clickedObjectIDs(searcher.index.indexName, eventName, objectIDs)
            }
        }
    }

    fun clickedFilters(eventName: EventName, filters: List<Filter.Facet>) {
        coroutineScope.launch(exceptionHandler) {
            response.value = withContext(Dispatchers.Default) {
                clickedFilters(searcher.index.indexName, eventName, filters)
            }
        }
    }
}