@file:Suppress("FunctionName")

package com.algolia.search.client

import com.algolia.client.api.SearchClient
import com.algolia.client.configuration.ClientOptions
import com.algolia.search.configuration.ConfigurationSearch
import io.ktor.client.plugins.logging.LogLevel

@Deprecated("Legacy client. Use com.algolia.client.api.SearchClient directly.")
public fun ClientSearch(
    appId: String,
    apiKey: String,
    logLevel: LogLevel = LogLevel.NONE,
): SearchClient = SearchClient(appId, apiKey)

@Deprecated("Legacy client. Use com.algolia.client.api.SearchClient directly.")
public fun ClientSearch(
    configuration: ConfigurationSearch,
): SearchClient =
    SearchClient(
        configuration.appId,
        configuration.apiKey,
        ClientOptions(
            engine = configuration.engine,
            logLevel = configuration.logLevel
        )
    )
