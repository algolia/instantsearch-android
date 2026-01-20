package com.algolia.search.configuration

import com.algolia.search.logging.LogLevel
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID

@Deprecated("Legacy configuration. Use v3 SearchClient directly.")
public data class ConfigurationSearch(
    val applicationID: ApplicationID,
    val apiKey: APIKey,
    val logLevel: LogLevel = LogLevel.None,
)

