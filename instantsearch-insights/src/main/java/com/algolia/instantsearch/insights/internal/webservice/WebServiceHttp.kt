package com.algolia.instantsearch.insights.internal.webservice

import com.algolia.search.client.ClientInsights
import com.algolia.search.configuration.ConfigurationInsights
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.insights.InsightsEvent
import io.ktor.client.statement.HttpResponse

internal class WebServiceHttp(
    private val appId: String,
    private val apiKey: String,
    private val connectTimeoutInMilliseconds: Long,
    private val readTimeoutInMilliseconds: Long,
) : WebService {

    private val clientInsights: ClientInsights =
        ClientInsights(
            ConfigurationInsights(
                applicationID = ApplicationID(appId),
                apiKey = APIKey(apiKey),
                writeTimeout = connectTimeoutInMilliseconds,
                readTimeout = readTimeoutInMilliseconds
            )
        )

    override suspend fun send(vararg events: InsightsEvent): HttpResponse {
        return clientInsights.sendEvents(events.toList())
    }

    override fun toString(): String {
        return "WebServiceHttp(appId='$appId', apiKey='$apiKey', connectTimeoutInMilliseconds=$connectTimeoutInMilliseconds, readTimeoutInMilliseconds=$readTimeoutInMilliseconds)"
    }
}
