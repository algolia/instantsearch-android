package com.algolia.search.configuration

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.logging.LogLevel

@Deprecated("Legacy configuration. Use v3 SearchClient directly.")
public data class ConfigurationSearch(
    val appId: String,
    val apiKey: String,
    val logLevel: LogLevel = LogLevel.NONE,
    val engine: HttpClientEngine? = null,
)
