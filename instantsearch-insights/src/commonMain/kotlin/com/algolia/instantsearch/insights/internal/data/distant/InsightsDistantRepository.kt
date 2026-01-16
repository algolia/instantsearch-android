package com.algolia.instantsearch.insights.internal.data.distant

import com.algolia.client.transport.RequestOptions
import com.algolia.instantsearch.insights.internal.event.EventResponse
import com.algolia.instantsearch.migration2to3.Credentials
import com.algolia.instantsearch.migration2to3.InsightsEvent
import kotlinx.serialization.json.JsonObject


internal interface InsightsDistantRepository : Credentials {

    suspend fun customPost(
        path: String,
        parameters: Map<kotlin.String, Any>? = null,
        body: JsonObject? = null,
        requestOptions: RequestOptions? = null,
    ): JsonObject


}
