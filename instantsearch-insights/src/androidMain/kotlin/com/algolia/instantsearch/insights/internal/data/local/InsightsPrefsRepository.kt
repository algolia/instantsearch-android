package com.algolia.instantsearch.insights.internal.data.local

import android.content.SharedPreferences
import com.algolia.instantsearch.insights.internal.data.local.mapper.InsightsEventDOMapper
import com.algolia.instantsearch.insights.internal.data.local.mapper.InsightsEventsMapper
import com.algolia.instantsearch.insights.internal.extension.events
import com.algolia.search.model.insights.InsightsEvent

internal class InsightsPrefsRepository(
    private val preferences: SharedPreferences,
) : InsightsLocalRepository {

    override fun append(event: InsightsEvent) {
        preferences.events = preferences.events
            .toMutableSet()
            .apply { add(event.asJsonString()) }
    }

    private fun InsightsEvent.asJsonString(): String {
        val eventDO = InsightsEventsMapper.map(this)
        return InsightsEventDOMapper.map(eventDO)
    }

    override fun overwrite(events: List<InsightsEvent>) {
        preferences.events = events
            .map(InsightsEventsMapper::map)
            .map(InsightsEventDOMapper::map)
            .toSet()
    }

    override fun read(): List<InsightsEvent> {
        return preferences.events
            .map(InsightsEventDOMapper::unmap)
            .map(InsightsEventsMapper::unmap)
    }

    override fun count(): Int {
        return preferences.events.size
    }

    override fun clear() {
        preferences.events = setOf()
    }
}
