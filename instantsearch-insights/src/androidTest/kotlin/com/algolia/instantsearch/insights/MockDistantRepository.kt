package com.algolia.instantsearch.insights

import com.algolia.instantsearch.insights.internal.data.distant.InsightsDistantRepository
import com.algolia.instantsearch.insights.internal.event.EventResponse
import com.algolia.search.model.insights.InsightsEvent

internal class MockDistantRepository : InsightsDistantRepository {

    var code = 200

    override suspend fun send(event: InsightsEvent): EventResponse {
        return EventResponse(event, code)
    }
}
