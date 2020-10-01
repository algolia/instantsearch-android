package com.algolia.instantsearch.insights.internal.saver

import com.algolia.search.model.insights.InsightsEvent

internal interface InsightsSaver {

    fun save(event: InsightsEvent)

    fun size(): Int
}
