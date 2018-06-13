package com.algolia.instantsearch.insights

import org.json.JSONObject


internal object ConverterStringToEvent : Converter<String, Event> {

    override fun convert(input: String): Event {
        val json = JSONObject(input)
        val type = EventType.valueOf(json[Type].toString())
        val params: Map<String, Any> = json.keys()
            .asSequence()
            .filterNot { it == Type }
            .map { it to json.get(it) }
            .toMap()

        return when (type) {
            EventType.View -> Event.View(params)
            EventType.Click -> Event.Click(params)
            EventType.Conversion -> Event.Conversion(params)
        }
    }
}
