package com.algolia.instantsearch.migration2to3

import io.ktor.client.statement.HttpResponse

public interface EndpointInsights {

    /**
     * Send one [InsightsEvent].
     */
    public suspend fun sendEvent(event: InsightsEvent, requestOptions: RequestOptions? = null): HttpResponse

    /**
     * Send multiple [InsightsEvent].
     */
    public suspend fun sendEvents(events: List<InsightsEvent>, requestOptions: RequestOptions? = null): HttpResponse
}
