package com.algolia.search.client

import com.algolia.client.api.InsightsClient
import com.algolia.client.configuration.ClientOptions
import com.algolia.search.configuration.ConfigurationInsights
import io.ktor.client.plugins.logging.LogLevel

@Deprecated("Legacy client. Use com.algolia.client.api.InsightsClient directly.")
public fun ClientInsights(
    appId: String,
    apiKey: String,
    logLevel: LogLevel = LogLevel.NONE,
): InsightsClient =
    InsightsClient(
        appId,
        apiKey,
        region = null,
        options = ClientOptions(logLevel = logLevel)
    )

@Deprecated("Legacy client. Use com.algolia.client.api.InsightsClient directly.")
public fun ClientInsights(
    configuration: ConfigurationInsights,
): InsightsClient =
    InsightsClient(
        configuration.appId,
        configuration.apiKey,
        region = null,
        options = ClientOptions(
            engine = configuration.engine,
            logLevel = configuration.logLevel
        )
    )
