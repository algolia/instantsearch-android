package com.algolia.instantsearch.insights.internal.converter

import com.algolia.instantsearch.insights.internal.event.EventInternal
import com.algolia.search.model.IndexName
import com.algolia.search.model.ObjectID
import com.algolia.search.model.insights.EventName
import com.algolia.search.model.insights.UserToken
import org.json.JSONArray
import org.json.JSONObject

internal object ConverterEventInternalToString : Converter<EventInternal, String> {

    override fun convert(input: EventInternal): String {
        return JSONObject().apply {
            input.entries.forEach { entry ->
                when (val value = entry.value) {
                    is Collection<*> -> put(entry.key, JSONArray(value.map{ it.toStringIfRaw() })) //TODO: temporary solution, to be replaced by InsightsClient
                    else -> put(entry.key, value.toStringIfRaw())
                }
            }
        }.toString()
    }
}
