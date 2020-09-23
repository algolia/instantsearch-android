package com.algolia.instantsearch.insights.event

import com.algolia.instantsearch.insights.internal.event.EventInternal

internal data class EventResponse(
    val event: EventInternal,
    val code: Int,
)
