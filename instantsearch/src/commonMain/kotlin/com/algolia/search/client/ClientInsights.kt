package com.algolia.search.client

import com.algolia.client.api.InsightsClient
import com.algolia.client.configuration.ClientOptions
import com.algolia.search.configuration.ConfigurationInsights
import com.algolia.search.logging.LogLevel
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import io.ktor.client.plugins.logging.LogLevel as KtorLogLevel

@Deprecated("Legacy client. Use com.algolia.client.api.InsightsClient directly.")
public fun ClientInsights(
    applicationID: ApplicationID,
    apiKey: APIKey,
    logLevel: LogLevel = LogLevel.None,
): InsightsClient =
    InsightsClient(
        applicationID,
        apiKey,
        region = null,
        options = ClientOptions(logLevel = logLevel.toKtorLogLevel())
    )

@Deprecated("Legacy client. Use com.algolia.client.api.InsightsClient directly.")
public fun ClientInsights(
    configuration: ConfigurationInsights,
): InsightsClient =
    InsightsClient(
        configuration.applicationID,
        configuration.apiKey,
        region = null,
        options = ClientOptions(
            engine = configuration.engine,
            logLevel = configuration.logLevel.toKtorLogLevel()
        )
    )

private fun LogLevel.toKtorLogLevel(): KtorLogLevel {
    return when (this) {
        LogLevel.All -> KtorLogLevel.ALL
        LogLevel.None -> KtorLogLevel.NONE
    }
}
