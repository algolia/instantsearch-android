package com.algolia.instantsearch.insights

import com.algolia.instantsearch.insights.internal.data.local.InsightsLocalRepository
import com.algolia.search.model.insights.InsightsEvent

internal class MockLocalRepository(
    private val events: MutableList<InsightsEvent>,
) : InsightsLocalRepository {

    override fun append(event: InsightsEvent) {
        events.add(event)
    }

    override fun overwrite(events: List<InsightsEvent>) {
        clear()
        this.events += events
    }

    override fun read(): List<InsightsEvent> {
        return events
    }

    override fun count(): Int {
        return events.size
    }

    override fun clear() {
        events.clear()
    }
}
