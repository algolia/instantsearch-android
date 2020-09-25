package com.algolia.instantsearch.insights.internal.extension

import com.algolia.instantsearch.insights.event.EventResponse
import com.algolia.instantsearch.insights.internal.database.Database
import com.algolia.instantsearch.insights.internal.logging.InsightsLogger
import com.algolia.instantsearch.insights.internal.webservice.WebService
import com.algolia.search.model.IndexName
import com.algolia.search.model.insights.InsightsEvent
import io.ktor.client.features.ClientRequestException
import io.ktor.http.isSuccess
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.runBlocking

internal fun WebService.sendEvent(event: InsightsEvent): EventResponse {
    return runBlocking {
        val (code: Int, message: String) = try {
            val response = send(event)
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
        EventResponse(
            code = code,
            event = event
        )
    }
}

internal fun WebService.sendEvents(events: List<InsightsEvent>): List<EventResponse> {
    return events.map { event -> sendEvent(event) }
}

internal fun WebService.uploadEvents(database: Database, indexName: IndexName): List<EventResponse> {
    val events = database.read()
    InsightsLogger.log(indexName, "Flushing remaining ${events.size} events.")
    val failedEvents = sendEvents(events).filterEventsWhenException()
    database.overwrite(failedEvents.map { it.event })
    return failedEvents
}
