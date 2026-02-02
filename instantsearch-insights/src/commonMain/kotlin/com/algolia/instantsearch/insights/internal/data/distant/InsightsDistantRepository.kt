
package com.algolia.instantsearch.insights.internal.data.distant

import com.algolia.client.transport.RequestOptions
import com.algolia.instantsearch.insights.internal.event.EventResponse
import com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO
import kotlinx.serialization.json.JsonObject

internal interface InsightsDistantRepository {
    val applicationID: String
    val apiKey: String

    suspend fun customPost(
        path: String,
        parameters: Map<kotlin.String, Any>? = null,
        body: JsonObject? = null,
        requestOptions: RequestOptions? = null,
    ): JsonObject

    suspend fun send(event: InsightsEventDO): EventResponse
}
