
package com.algolia.instantsearch.insights.internal.cache

import com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO

internal interface InsightsCache {

    fun save(event: InsightsEventDO)

    fun size(): Int
}
