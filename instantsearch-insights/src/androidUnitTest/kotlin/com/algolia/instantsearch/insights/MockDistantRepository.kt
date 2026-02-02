package com.algolia.instantsearch.insights

import com.algolia.instantsearch.insights.internal.data.distant.InsightsDistantRepository
import com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO
import com.algolia.instantsearch.insights.internal.event.EventResponse
import kotlinx.serialization.json.JsonObject

internal class MockDistantRepository : InsightsDistantRepository {

    var code = 200

    override suspend fun customPost(
        path: String,
        parameters: Map<String, Any>?,
        body: JsonObject?,
        requestOptions: com.algolia.client.transport.RequestOptions?,
    ): JsonObject = JsonObject(emptyMap())

    override suspend fun send(event: InsightsEventDO): EventResponse {
        return EventResponse(event, code)
    }

    override val apiKey = "apiKey"
    override val applicationID = "applicationID"
}
