
package com.algolia.instantsearch.insights.internal.data.local

import android.content.SharedPreferences
import com.algolia.instantsearch.insights.internal.data.local.mapper.InsightsEventDOMapper
import com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO
import com.algolia.instantsearch.insights.internal.extension.events

internal class InsightsPrefsRepository(
    private val preferences: SharedPreferences,
) : InsightsLocalRepository {

    override fun append(event: InsightsEventDO) {
        preferences.events = preferences.events
            .toMutableSet()
            .apply { add(InsightsEventDOMapper.map(event)) }
    }

    override fun overwrite(events: List<InsightsEventDO>) {
        preferences.events = events
            .map(InsightsEventDOMapper::map)
            .toSet()
    }

    override fun read(): List<InsightsEventDO> {
        return preferences.events
            .map(InsightsEventDOMapper::unmap)
    }

    override fun count(): Int {
        return preferences.events.size
    }

    override fun clear() {
        preferences.events = setOf()
    }
}
