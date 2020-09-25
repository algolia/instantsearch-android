package com.algolia.instantsearch.insights.event

import com.algolia.search.model.insights.InsightsEvent

internal data class EventResponse(
    val event: InsightsEvent,
    val code: Int,
)
