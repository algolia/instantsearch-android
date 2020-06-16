package com.algolia.instantsearch.insights.internal.webservice

import android.os.Build
import com.algolia.instantsearch.insights.BuildConfig
import com.algolia.instantsearch.insights.event.EventResponse
import com.algolia.instantsearch.insights.internal.InsightsLogger
import com.algolia.instantsearch.insights.internal.database.Database
import com.algolia.instantsearch.insights.internal.event.EventInternal

internal fun List<EventResponse>.filterEventsWhenException(): List<EventResponse> {
    return this.filter { it.code == -1 }
}

internal fun Int.isValidHttpCode() = this == 200 || this == 201

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

internal data class LibraryVersion(
    val name: String,
    val version: String
)

/**
 * Computing the User Agent. Expected output: insights-android (2.1.0); Android (12.1.0)
 */
internal fun computeUserAgent(): String {

    val insightsVersion = LibraryVersion(
        name = "insights-android",
        version = BuildConfig.INSIGHTS_VERSION
    )
    val androidVersion = LibraryVersion(
        name = "Android",
        version = "${Build.VERSION.SDK_INT}.0.0"
    )
    val headers = arrayOf(insightsVersion, androidVersion)

    return headers.joinToString(separator = "; ") {
        "${it.name} (${it.version})"
    }
}
