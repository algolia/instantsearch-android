
package com.algolia.instantsearch.insights.internal.cache

import com.algolia.instantsearch.insights.internal.data.local.InsightsLocalRepository
import com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO

internal class InsightsEventCache(
    private val localRepository: InsightsLocalRepository,
) : InsightsCache {

    override fun save(event: InsightsEventDO) {
        localRepository.append(event)
    }

    override fun size(): Int {
        return localRepository.count()
    }
}
