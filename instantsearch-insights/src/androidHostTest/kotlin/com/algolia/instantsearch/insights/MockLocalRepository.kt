package com.algolia.instantsearch.insights

import com.algolia.instantsearch.insights.internal.data.local.InsightsLocalRepository
import com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO

internal class MockLocalRepository(
    private val events: MutableList<InsightsEventDO>,
) : InsightsLocalRepository {

    override fun append(event: InsightsEventDO) {
        events.add(event)
    }

    override fun overwrite(events: List<InsightsEventDO>) {
        clear()
        this.events += events
    }

    override fun read(): List<InsightsEventDO> {
        return events
    }

    override fun count(): Int {
        return events.size
    }

    override fun clear() {
        events.clear()
    }
}
