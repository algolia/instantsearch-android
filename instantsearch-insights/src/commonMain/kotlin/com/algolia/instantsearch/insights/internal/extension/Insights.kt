package com.algolia.instantsearch.insights.internal.extension

import com.algolia.instantsearch.insights.Insights
import com.algolia.instantsearch.insights.internal.data.settings.InsightsSettings
import com.algolia.instantsearch.insights.internal.logging.InsightsLogger
import com.algolia.search.client.ClientInsights
import com.algolia.search.configuration.ConfigurationInsights
import com.algolia.search.logging.LogLevel
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.insights.UserToken

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
    val userToken = UserToken(settings.storedUserToken())
    InsightsLogger.log("Insights user token: $userToken")
    return Insights.Configuration(5000, 5000, userToken)
}

/**
 * Get user token if not null, otherwise generate and store a new one.
 */
private fun InsightsSettings.storedUserToken(): String {
    return userToken ?: randomUUID().also { userToken = it }
}
