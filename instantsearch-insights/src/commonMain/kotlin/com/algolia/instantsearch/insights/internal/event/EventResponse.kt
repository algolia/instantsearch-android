
package com.algolia.instantsearch.insights.internal.event

import com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO

internal data class EventResponse(
    val event: InsightsEventDO,
    val code: Int,
) {

    companion object {
        /** The request could not be performed (e.g. network error); the event should be retried. */
        const val CODE_EXCEPTION = -1

        /** The event could not be converted into an Insights API event; retrying would never succeed. */
        const val CODE_UNMAPPABLE = -2
    }
}
