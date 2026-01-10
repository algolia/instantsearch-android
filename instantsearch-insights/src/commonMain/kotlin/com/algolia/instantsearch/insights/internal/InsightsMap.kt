package com.algolia.instantsearch.insights.internal

import com.algolia.instantsearch.migration2to3.IndexName

/**
 * Map storing all registered Insights instances.
 */
internal object InsightsMap : MutableMap<IndexName, InsightsController> by mutableMapOf()
