@file:Suppress("FunctionName")

package com.algolia.search.client

import com.algolia.client.api.SearchClient
import com.algolia.search.configuration.ConfigurationSearch
import com.algolia.search.logging.LogLevel
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName

@Deprecated("Legacy client. Use com.algolia.client.api.SearchClient directly.")
public fun ClientSearch(
    applicationID: ApplicationID,
    apiKey: APIKey,
    logLevel: LogLevel = LogLevel.None,
): SearchClient = SearchClient(applicationID, apiKey)

@Deprecated("Legacy client. Use com.algolia.client.api.SearchClient directly.")
public fun ClientSearch(
    configuration: ConfigurationSearch,
): SearchClient = SearchClient(configuration.applicationID, configuration.apiKey)

@Deprecated("Legacy index wrapper for examples only.")
public data class Index(val indexName: IndexName)

@Deprecated("Legacy initIndex helper for examples only.")
public fun SearchClient.initIndex(indexName: IndexName): Index = Index(indexName)

