package com.algolia.instantsearch.insights.internal

import com.algolia.search.model.IndexName

/**
 * Map storing all registered Insights instances.
 */
internal object InsightsMap : MutableMap<IndexName, InsightsController> by mutableMapOf()
