package com.algolia.instantsearch.insights.internal

/**
 * Map storing all registered Insights instances.
 */
internal object InsightsMap : MutableMap<String, InsightsController> by mutableMapOf()
