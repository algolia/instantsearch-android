package com.algolia.search.configuration

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.logging.LogLevel

@Deprecated("Legacy configuration. Use v3 InsightsClient directly.")
public data class ConfigurationInsights(
    val appId: String,
    val apiKey: String,
    val logLevel: LogLevel = LogLevel.NONE,
    val engine: HttpClientEngine? = null,
)
