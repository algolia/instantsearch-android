package com.algolia.instantsearch.insights.internal.extension

import com.algolia.client.model.querysuggestions.LogLevel
import com.algolia.instantsearch.insights.Insights
import com.algolia.instantsearch.insights.internal.data.settings.InsightsSettings
import com.algolia.instantsearch.insights.internal.logging.InsightsLogger
import com.algolia.instantsearch.migration2to3.APIKey
import com.algolia.instantsearch.migration2to3.ApplicationID
import com.algolia.instantsearch.migration2to3.ClientInsights
import com.algolia.instantsearch.migration2to3.ConfigurationInsights
import com.algolia.instantsearch.migration2to3.UserToken

/**
 * Create new Insights API Client.
 */
internal fun clientInsights(
    appId: ApplicationID,
    apiKey: APIKey,
    configuration: Insights.Configuration,
    clientLogLevel: LogLevel,
): ClientInsights {
    return ClientInsights(
        ConfigurationInsights(
            applicationID = appId,
            apiKey = apiKey,
            writeTimeout = configuration.connectTimeoutInMilliseconds,
            readTimeout = configuration.readTimeoutInMilliseconds,
            logLevel = clientLogLevel
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
