package com.algolia.instantsearch.insights.internal.saver

import com.algolia.instantsearch.insights.internal.data.local.InsightsLocalRepository
import com.algolia.search.model.insights.InsightsEvent

internal class InsightsEventSaver(
    private val localRepository: InsightsLocalRepository,
) : InsightsSaver {

    override fun save(event: InsightsEvent) {
        localRepository.append(event)
    }

    override fun size(): Int {
        return localRepository.count()
    }
}
