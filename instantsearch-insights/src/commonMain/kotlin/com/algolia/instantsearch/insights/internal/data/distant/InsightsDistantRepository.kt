package com.algolia.instantsearch.insights.internal.data.distant

import com.algolia.instantsearch.insights.internal.event.EventResponse
import com.algolia.search.model.insights.InsightsEvent

internal interface InsightsDistantRepository {

    suspend fun send(event: InsightsEvent): EventResponse
}
