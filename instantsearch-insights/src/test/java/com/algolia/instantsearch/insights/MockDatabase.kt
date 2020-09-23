package com.algolia.instantsearch.insights

import com.algolia.instantsearch.insights.internal.database.Database
import com.algolia.instantsearch.insights.internal.event.EventInternal
import com.algolia.search.model.IndexName

internal class MockDatabase(
    override val indexName: IndexName,
    private val events: MutableList<EventInternal>,
) : Database {

    override fun append(event: EventInternal) {
        events.add(event)
    }

    override fun overwrite(events: List<EventInternal>) {
        clear()
        this.events += events
    }

    override fun read(): List<EventInternal> {
        return events
    }

    override fun count(): Int {
        return events.size
    }

    override fun clear() {
        events.clear()
    }
}
