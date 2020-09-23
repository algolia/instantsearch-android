package com.algolia.instantsearch.insights.internal.extension

import com.algolia.instantsearch.insights.event.EventResponse

internal fun List<EventResponse>.filterEventsWhenException(): List<EventResponse> {
    return this.filter { it.code == -1 }
}

internal fun Int.isValidHttpCode() = this == 200 || this == 201
