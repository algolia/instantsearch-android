
package com.algolia.instantsearch.insights.internal.data.distant

import com.algolia.client.api.InsightsClient
import com.algolia.client.exception.AlgoliaApiException
import com.algolia.client.model.insights.InsightsEvents
import com.algolia.client.transport.RequestOptions
import com.algolia.instantsearch.insights.internal.data.local.mapper.InsightsEventsMapper
import com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO
import com.algolia.instantsearch.insights.internal.event.EventResponse
import com.algolia.instantsearch.insights.internal.logging.InsightsLogger
import com.algolia.instantsearch.util.algoliaAgent
import kotlinx.serialization.json.JsonObject

internal class InsightsHttpRepository(
    private val insightsClient: InsightsClient,
) : InsightsDistantRepository {

    override val applicationID: String get() = insightsClient.appId
    override val apiKey: String get() = insightsClient.apiKey

    private val requestOptions = mapOf<String, String>(
        "X-Algolia-Agent" to algoliaAgent("Algolia insights for Android")
    )
    private val defaultRequestOptions = RequestOptions(headers = requestOptions)

    override suspend fun customPost(
        path: String,
        parameters: Map<String, Any>?,
        body: JsonObject?,
        requestOptions: RequestOptions?
    ): JsonObject {
        return insightsClient.customPost(
            path = path,
            parameters = parameters,
            body = body,
            requestOptions = defaultRequestOptions + requestOptions
        )
    }

    override suspend fun send(event: InsightsEventDO): EventResponse {
        val eventsItem = InsightsEventsMapper.doToEventsItem(event) ?: run {
            InsightsLogger.log(event.indexName, "Failed to map event for Insights.")
            return EventResponse(event, -1)
        }
        val (code, message) = try {
            val response = insightsClient.pushEvents(
                insightsEvents = InsightsEvents(listOf(eventsItem)),
                requestOptions = defaultRequestOptions
            )
            val status = response.status ?: 200
            status to (response.message ?: "Sync succeeded for $event.")
        } catch (exception: Exception) {
            val status = (exception as? AlgoliaApiException)?.httpErrorCode ?: -1
            status to exception.message.orEmpty()
        }
        InsightsLogger.log(event.indexName, message)
        return EventResponse(event, code)
    }
}
