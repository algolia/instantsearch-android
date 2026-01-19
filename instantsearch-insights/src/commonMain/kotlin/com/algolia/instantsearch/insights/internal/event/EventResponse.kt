
package com.algolia.instantsearch.insights.internal.event

import com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO

internal data class EventResponse(
    val event: InsightsEventDO,
    val code: Int,
)
