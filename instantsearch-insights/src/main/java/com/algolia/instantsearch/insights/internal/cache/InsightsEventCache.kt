package com.algolia.instantsearch.insights.internal.cache

import com.algolia.instantsearch.insights.internal.data.local.InsightsLocalRepository
import com.algolia.search.model.insights.InsightsEvent

internal class InsightsEventCache(
    private val localRepository: InsightsLocalRepository,
) : InsightsCache {

    override fun save(event: InsightsEvent) {
        localRepository.append(event)
    }

    override fun size(): Int {
        return localRepository.count()
    }
}
