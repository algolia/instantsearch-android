package com.algolia.instantsearch.migration2to3

import com.algolia.client.api.InsightsClient

/**
 * Client to manage [InsightsEvent].
 */
@Deprecated("Replace with client code", ReplaceWith("InsightsClient"))
public typealias ClientInsights = InsightsClient
