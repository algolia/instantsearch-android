package com.algolia.instantsearch.insights

import org.json.JSONObject


internal object ConverterParameterToString : Converter<EventParameters, String> {

    override fun convert(input: EventParameters): String {
        return JSONObject().also { json ->
            input.entries.forEach { json.put(it.key, it.value) }
        }.toString()
    }
}
