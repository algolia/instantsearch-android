package com.algolia.instantsearch.insights

import android.content.SharedPreferences


internal fun SharedPreferences.consumeEvents(
    eventConsumer: (List<String>) -> List<NetworkResponse>
): List<NetworkResponse> {
    val networkResponses = eventConsumer(events.toList())
    val failedEvents = networkResponses.filter { it.code != 200 }

    this.events = failedEvents.map { it.serializedEvent }.toSet()
    return failedEvents
}

internal fun NetworkManager.eventConsumer(): (List<String>) -> List<NetworkResponse> {
    return { serializedEvents ->
        serializedEvents.map {
            val event = ConverterStringToEvent.convert(it)
            NetworkResponse(
                code = sendEvent(event),
                serializedEvent = it
            )
        }
    }
}
