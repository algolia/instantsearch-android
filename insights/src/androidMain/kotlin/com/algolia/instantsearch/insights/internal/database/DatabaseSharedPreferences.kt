package com.algolia.instantsearch.insights.internal.database

import android.content.Context
import com.algolia.instantsearch.insights.internal.converter.ConverterEventInternalToString
import com.algolia.instantsearch.insights.internal.converter.ConverterStringToEventInternal
import com.algolia.instantsearch.insights.internal.event.EventInternal
import com.algolia.instantsearch.insights.internal.extension.events
import com.algolia.instantsearch.insights.internal.extension.sharedPreferences

internal class DatabaseSharedPreferences(
    context: Context,
    override val indexName: String
) : Database {

    private val preferences = context.sharedPreferences(prefixAlgolia(indexName))

    private fun prefixAlgolia(string: String): String = "Algolia Insights-$string"

    public override fun append(event: EventInternal) {
        val events = preferences.events
            .toMutableSet()
            .also { it.add(ConverterEventInternalToString.convert(event)) }

        preferences.events = events
    }

    public override fun overwrite(events: List<EventInternal>) {
        preferences.events = ConverterEventInternalToString.convert(events).toSet()
    }

    public override fun read(): List<EventInternal> {
        return ConverterStringToEventInternal.convert(preferences.events.toList())
    }

    public override fun count(): Int {
        return preferences.events.size
    }

    public override fun clear() {
        preferences.events = setOf()
    }
}
