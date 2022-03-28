package com.algolia.instantsearch.insights.internal.data.distant

import com.algolia.instantsearch.insights.internal.event.EventResponse
import com.algolia.instantsearch.insights.internal.logging.InsightsLogger
import com.algolia.search.client.ClientInsights
import com.algolia.search.configuration.Credentials
import com.algolia.search.model.insights.InsightsEvent
import io.ktor.client.features.ClientRequestException
import io.ktor.http.isSuccess
import io.ktor.utils.io.readUTF8Line

internal class InsightsHttpRepository(
    private val clientInsights: ClientInsights,
) : InsightsDistantRepository, Credentials by clientInsights {

    override suspend fun send(event: InsightsEvent): EventResponse {
        val (code: Int, message: String) = try {
            val response = clientInsights.sendEvent(event)
            val message = when {
                response.status.isSuccess() -> "Sync succeeded for $event.\""
                else -> response.content.readUTF8Line().orEmpty()
            }
            response.status.value to message
        } catch (exception: Exception) {
            val status = (exception as? ClientRequestException)?.response?.status?.value ?: -1
            status to exception.message.orEmpty()
        }
        InsightsLogger.log(event.indexName, message)
        return EventResponse(code = code, event = event)
    }
}
