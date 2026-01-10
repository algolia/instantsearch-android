package com.algolia.instantsearch.migration2to3

import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

internal val JsonNoDefaults = Json {
    ignoreUnknownKeys = true
}

@InternalSerializationApi @Serializable
internal data class RequestInsightsEvents(
    @SerialName(Key.Events) val events: List<InsightsEvent>
)

internal class EndpointInsightsImpl(
    private val transport: Transport,
) : EndpointInsights {

    override suspend fun sendEvent(event: InsightsEvent, requestOptions: RequestOptions?): HttpResponse {
        return sendEvents(listOf(event), requestOptions)
    }

    override suspend fun sendEvents(events: List<InsightsEvent>, requestOptions: RequestOptions?): HttpResponse {
        val body = JsonNoDefaults.encodeToString(RequestInsightsEvents.serializer(), RequestInsightsEvents(events))

        return transport.request(HttpMethod.Post, CallType.Write, Route.EventsV1, requestOptions, body)
    }
}

/**
 * Create an [EndpointInsights] instance.
 */
internal fun EndpointInsights(
    transport: Transport,
): EndpointInsights = EndpointInsightsImpl(transport)
