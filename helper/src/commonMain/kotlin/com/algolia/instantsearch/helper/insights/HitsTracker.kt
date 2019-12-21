package com.algolia.instantsearch.helper.insights

import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.helper.searcher.SearcherScope
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.client.ClientInsights
import com.algolia.search.endpoint.EndpointInsightsUser
import com.algolia.search.model.ObjectID
import com.algolia.search.model.insights.EventName
import com.algolia.search.model.insights.UserToken
import io.ktor.client.response.HttpResponse
import kotlinx.coroutines.*


public class HitsTracker(
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

    init {
        searcher.query.clickAnalytics = true
    }

    public fun clickedObjectID(eventName: EventName, objectIDs: List<ObjectID>, positions: List<Int>) {
        coroutineScope.launch(exceptionHandler) {
            response.value = withContext(Dispatchers.Default) {
                val currentQueryID = searcher.response.value?.queryID

                if (currentQueryID != null) {
                    clickedObjectIDsAfterSearch(
                        searcher.index.indexName,
                        eventName,
                        currentQueryID,
                        objectIDs,
                        positions
                    )
                } else {
                    clickedObjectIDs(searcher.index.indexName, eventName, objectIDs)
                }
            }
        }
    }

    public fun viewedObjectID(eventName: EventName, objectID: ObjectID) {
        coroutineScope.launch(exceptionHandler) {
            response.value = withContext(Dispatchers.Default) {
                viewedObjectIDs(searcher.index.indexName, eventName, listOf(objectID))
            }
        }
    }


    public fun convertedObjectIDs(eventName: EventName, objectIDs: List<ObjectID>) {
        coroutineScope.launch(exceptionHandler) {
            response.value = withContext(Dispatchers.Default) {
                val currentQueryID = searcher.response.value?.queryID

                if (currentQueryID != null) {
                    convertedObjectIDsAfterSearch(searcher.index.indexName, eventName, currentQueryID, objectIDs)
                } else convertedObjectIDs(searcher.index.indexName, eventName, objectIDs)
            }
        }
    }

}