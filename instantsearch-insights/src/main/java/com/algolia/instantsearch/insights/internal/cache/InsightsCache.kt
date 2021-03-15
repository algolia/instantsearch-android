package com.algolia.instantsearch.insights.internal.cache

import com.algolia.search.model.insights.InsightsEvent

internal interface InsightsCache {

    fun save(event: InsightsEvent)

    fun size(): Int
}
