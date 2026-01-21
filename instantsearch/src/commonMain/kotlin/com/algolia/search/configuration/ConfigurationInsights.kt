package com.algolia.search.configuration

import com.algolia.search.logging.LogLevel
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import io.ktor.client.engine.HttpClientEngine

@Deprecated("Legacy configuration. Use v3 InsightsClient directly.")
public data class ConfigurationInsights(
    val applicationID: ApplicationID,
    val apiKey: APIKey,
    val logLevel: LogLevel = LogLevel.None,
    val engine: HttpClientEngine? = null,
)
