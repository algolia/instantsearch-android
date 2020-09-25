package com.algolia.instantsearch.insights.internal.database

import android.content.Context
import com.algolia.instantsearch.insights.internal.database.mapper.InsightsEventDOMapper
import com.algolia.instantsearch.insights.internal.database.mapper.InsightsEventsMapper
import com.algolia.instantsearch.insights.internal.extension.events
import com.algolia.instantsearch.insights.internal.extension.sharedPreferences
import com.algolia.search.model.IndexName
import com.algolia.search.model.insights.InsightsEvent

internal class DatabaseSharedPreferences(
    context: Context,
    override val indexName: IndexName,
) : Database {

    private val preferences = context.sharedPreferences(prefixAlgolia(indexName))

    private fun prefixAlgolia(string: IndexName): String = "Algolia Insights-$string"

    override fun append(event: InsightsEvent) {
        val events = preferences.events
            .toMutableSet()
            .also {
                val eventDO = InsightsEventsMapper.map(event)
                val eventDOString = InsightsEventDOMapper.map(eventDO)
                it.add(eventDOString)
            }

        preferences.events = events
    }

    override fun overwrite(events: List<InsightsEvent>) {
        preferences.events = events.map {
            val eventDO = InsightsEventsMapper.map(it)
            InsightsEventDOMapper.map(eventDO)
        }.toSet()
    }

    override fun read(): List<InsightsEvent> {
        return preferences.events.map {
            val eventDOString = InsightsEventDOMapper.unmap(it)
            InsightsEventsMapper.unmap(eventDOString)
        }
    }

    override fun count(): Int {
        return preferences.events.size
    }

    override fun clear() {
        preferences.events = setOf()
    }
}
