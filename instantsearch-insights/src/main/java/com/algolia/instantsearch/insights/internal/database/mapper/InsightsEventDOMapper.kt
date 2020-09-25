package com.algolia.instantsearch.insights.internal.database.mapper

import com.algolia.instantsearch.insights.internal.database.model.InsightsEventDO
import org.json.JSONArray
import org.json.JSONObject

internal object InsightsEventDOMapper : Mapper<InsightsEventDO, String> {

    override fun map(input: InsightsEventDO): String {
        return JSONObject().apply {
            input.entries.forEach { entry ->
                entry.value?.let {
                    when (it) {
                        is Collection<*> -> put(entry.key, JSONArray(it))
                        is Map<*, *> -> {
                            put(entry.key, JSONObject(it))
                        }
                        else -> put(entry.key, it)
                    }
                }
            }
        }.toString()
    }

    override fun unmap(input: String): InsightsEventDO {
        return JSONObject(input).toMap()
    }

    private fun JSONObject.toMap(): Map<String, Any?> {
        return keys()
            .asSequence()
            .map { jsonMap(it, this) }
            .toMap()
    }

    private fun jsonMap(key: String, jsonObject: JSONObject): Pair<String, Any?> {
        return when (jsonObject.get(key)) {
            is JSONArray -> key to jsonObject.getJSONArray(key).toList()
            is JSONObject -> key to jsonObject.getJSONObject(key).toMap()
            else -> key to jsonObject.get(key)
        }
    }

    private fun JSONArray.toList(): List<Any> {
        return mutableListOf<Any>().also {
            for (i in 0 until this.length()) {
                when (val element = this[i]) {
                    is JSONObject -> it.add(element.toMap())
                    is JSONArray -> it.add(it.toList())
                    else -> it.add(this[i])
                }
            }
        }
    }
}
