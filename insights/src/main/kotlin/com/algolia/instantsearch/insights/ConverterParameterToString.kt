package com.algolia.instantsearch.insights

import org.json.JSONObject


internal object ConverterParameterToString : Converter<Map<String, Any>, String> {

    override fun convert(input: Map<String, Any>): String {
        return JSONObject().also { json ->
            input.entries.forEach { json.put(it.key, it.value) }
        }.toString()
    }
}
