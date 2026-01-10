package com.algolia.instantsearch.insights.internal.event

import com.algolia.instantsearch.migration2to3.InsightsEvent

internal data class EventResponse(
    val event: InsightsEvent,
    val code: Int,
)
