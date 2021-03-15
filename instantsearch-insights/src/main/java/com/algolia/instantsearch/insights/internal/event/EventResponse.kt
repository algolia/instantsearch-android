package com.algolia.instantsearch.insights.internal.event

import com.algolia.search.model.insights.InsightsEvent

internal data class EventResponse(
    val event: InsightsEvent,
    val code: Int,
)
