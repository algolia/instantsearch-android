package com.algolia.instantsearch.insights.internal.data.distant

import com.algolia.client.api.InsightsClient
import com.algolia.client.exception.AlgoliaApiException
import com.algolia.client.transport.RequestOptions
import com.algolia.instantsearch.insights.internal.event.EventResponse
import com.algolia.instantsearch.insights.internal.logging.InsightsLogger
import com.algolia.instantsearch.migration2to3.Credentials
import com.algolia.instantsearch.migration2to3.InsightsEvent
import com.algolia.instantsearch.util.algoliaAgent
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.serialization.json.JsonObject

internal class InsightsHttpRepository(
    private val insightsClient: InsightsClient,
) : InsightsDistantRepository by insightsClient {

    private val requestOptions = mapOf<String, String>(
        "X-Algolia-Agent" to algoliaAgent("Algolia insights for Android")
    )

    override suspend fun customPost(
        path: String,
        parameters: Map<String, Any>?,
        body: JsonObject?,
        requestOptions: RequestOptions?
    ): JsonObject {
        TODO("Not yet implemented")
    }

    suspend fun send(event: InsightsEvent): EventResponse {
        TODO()
//        val (code: Int, message: String) = try {
//            val response = insightsClient.customPost(event, requestOptions)
//            val message = when {
//                response.status.isSuccess() -> "Sync succeeded for $event.\""
//                else -> response.bodyAsText()
//            }
//            response.status.value to message
//        } catch (exception: Exception) {
//            val status = (exception as? AlgoliaApiException)?.httpErrorCode ?: -1
//            status to exception.message.orEmpty()
//        }
        InsightsLogger.log(event.indexName, "message")
        return EventResponse(code = 123, event = event)
    }
}
