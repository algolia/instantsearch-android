package com.algolia.instantsearch.insights.internal.extension

import com.algolia.client.api.InsightsClient
import com.algolia.client.configuration.ClientOptions
import com.algolia.instantsearch.insights.Insights
import com.algolia.instantsearch.insights.internal.data.settings.InsightsSettings
import com.algolia.instantsearch.insights.internal.logging.InsightsLogger
import io.ktor.client.plugins.logging.LogLevel
import kotlin.time.DurationUnit
import kotlin.time.toDuration

/**
 * Create new Insights API Client.
 */
internal fun insightsClient(
    appId: String,
    apiKey: String,
    configuration: Insights.Configuration,
    clientLogLevel: LogLevel,
): InsightsClient {
    return InsightsClient(
        appId = appId,
        apiKey = apiKey,
        options = ClientOptions(
            writeTimeout = configuration.connectTimeoutInMilliseconds.toDuration(DurationUnit.MILLISECONDS),
            readTimeout = configuration.readTimeoutInMilliseconds.toDuration(DurationUnit.MILLISECONDS),
            logLevel = clientLogLevel,
        )
    )
}

/**
 * Generate a default insights configuration.
 */
internal fun defaultConfiguration(settings: InsightsSettings): Insights.Configuration {
    val userToken = settings.storedUserToken()
    InsightsLogger.log("Insights user token: $userToken")
    return Insights.Configuration(defaultUserToken = userToken)
}

/**
 * Get user token if not null, otherwise generate and store a new one.
 */
private fun InsightsSettings.storedUserToken(): String {
    return userToken ?: randomUUID().also { userToken = it }
}
