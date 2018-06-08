package com.algolia.instantsearch.insights

import org.json.JSONObject


internal object ConverterEventToString : Converter<Event, String> {

    override fun convert(input: Event): String {
        val type = when (input) {
            is Event.Click -> EventType.Click.name
            is Event.View -> EventType.View.name
            is Event.Conversion -> EventType.Conversion.name
        }
        return JSONObject().also { json ->
            json.put(Type, type)
            input.params.entries.forEach { json.put(it.key, it.value) }
        }.toString()
    }
}
