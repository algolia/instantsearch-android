
package com.algolia.instantsearch.insights.internal.data.local

import com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO

internal interface InsightsLocalRepository {

    fun append(event: InsightsEventDO)

    fun overwrite(events: List<InsightsEventDO>)

    fun read(): List<InsightsEventDO>

    fun count(): Int

    fun clear()
}
