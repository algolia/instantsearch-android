package com.algolia.instantsearch.insights.internal.cache

import com.algolia.instantsearch.migration2to3.InsightsEvent

internal interface InsightsCache {

    fun save(event: InsightsEvent)

    fun size(): Int
}
