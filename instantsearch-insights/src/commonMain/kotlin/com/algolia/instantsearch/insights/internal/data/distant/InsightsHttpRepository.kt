package com.algolia.instantsearch.insights.internal.data.distant

import com.algolia.instantsearch.insights.internal.event.EventResponse
import com.algolia.instantsearch.insights.internal.logging.InsightsLogger
import com.algolia.instantsearch.util.algoliaAgent
import com.algolia.search.client.ClientInsights
import com.algolia.search.configuration.Credentials
import com.algolia.search.dsl.requestOptions
import com.algolia.search.exception.AlgoliaApiException
import com.algolia.search.model.insights.InsightsEvent
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess

internal class InsightsHttpRepository(
    private val clientInsights: ClientInsights,
) : InsightsDistantRepository, Credentials by clientInsights {

    private val requestOptions = requestOptions {
        parameter("X-Algolia-Agent", algoliaAgent("Algolia insights for Android"))
    }

    override suspend fun send(event: InsightsEvent): EventResponse {
        val (code: Int, message: String) = try {
            val response = clientInsights.sendEvent(event, requestOptions)
            val message = when {
                response.status.isSuccess() -> "Sync succeeded for $event.\""
                else -> response.bodyAsText()
            }
            response.status.value to message
        } catch (exception: Exception) {
            val status = (exception as? AlgoliaApiException)?.httpErrorCode ?: -1
            status to exception.message.orEmpty()
        }
        InsightsLogger.log(event.indexName, message)
        return EventResponse(code = code, event = event)
    }
}
