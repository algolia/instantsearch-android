package com.algolia.instantsearch.insights.internal.extension

import com.algolia.instantsearch.insights.event.EventResponse
import com.algolia.instantsearch.insights.internal.InsightsLogger
import com.algolia.instantsearch.insights.internal.database.Database
import com.algolia.instantsearch.insights.internal.event.EventInternal
import com.algolia.instantsearch.insights.internal.webservice.WebService

internal fun WebService.sendEvent(indexName: String, event: EventInternal): EventResponse {
    val (errorMessage, code) = try {
        send(event)
    } catch (exception: Exception) {
        WebService.Response(
            exception.localizedMessage,
            -1
        )
    }
    val message = if (code.isValidHttpCode()) {
        "Sync succeeded for $event."
    } else {
        "$errorMessage (Code: $code)"
    }
    InsightsLogger.log(indexName, message)
    return EventResponse(
        code = code,
        event = event
    )
}

internal fun WebService.sendEvents(indexName: String, events: List<EventInternal>): List<EventResponse> {
    return events.map { event -> sendEvent(indexName, event) }
}


internal fun WebService.uploadEvents(database: Database, indexName: String): List<EventResponse> {
    val events = database.read()

    InsightsLogger.log(indexName, "Flushing remaining ${events.size} events.")

    val failedEvents = sendEvents(indexName, events).filterEventsWhenException()

    database.overwrite(failedEvents.map { it.event })
    return failedEvents
}
