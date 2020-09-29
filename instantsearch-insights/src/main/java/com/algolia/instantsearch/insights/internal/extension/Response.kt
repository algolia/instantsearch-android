package com.algolia.instantsearch.insights.internal.extension

import com.algolia.instantsearch.insights.internal.event.EventResponse

internal fun List<EventResponse>.filterEventsWhenException(): List<EventResponse> {
    return this.filter { it.code == -1 }
}
