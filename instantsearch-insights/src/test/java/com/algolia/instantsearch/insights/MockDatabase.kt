package com.algolia.instantsearch.insights

import com.algolia.instantsearch.insights.internal.database.Database
import com.algolia.search.model.IndexName
import com.algolia.search.model.insights.InsightsEvent

internal class MockDatabase(
    override val indexName: IndexName,
    private val events: MutableList<InsightsEvent>,
) : Database {

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
