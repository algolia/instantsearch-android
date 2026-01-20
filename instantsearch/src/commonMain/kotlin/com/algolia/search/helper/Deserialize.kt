package com.algolia.search.helper

import com.algolia.client.model.search.Hit
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement

@Deprecated("Legacy helper. Use v3 Hit parsing utilities instead.")
public fun <T> List<Hit>.deserialize(
    serializer: KSerializer<T>,
    json: Json = Json { ignoreUnknownKeys = true },
): List<T> {
    return map { hit ->
        val element: JsonObject = buildJsonObject {
            put("objectID", JsonPrimitive(hit.objectID))
            hit.additionalProperties?.forEach { (key, value) -> put(key, value) }
            hit.highlightResult?.let { put("_highlightResult", json.encodeToJsonElement(it)) }
            hit.snippetResult?.let { put("_snippetResult", json.encodeToJsonElement(it)) }
            hit.rankingInfo?.let { put("_rankingInfo", json.encodeToJsonElement(it)) }
            hit.distinctSeqID?.let { put("_distinctSeqID", json.encodeToJsonElement(it)) }
        }
        json.decodeFromJsonElement(serializer, element)
    }
}

@Deprecated("Legacy helper. Use v3 Hit parsing utilities instead.")
public fun <T> Hit.deserialize(
    serializer: KSerializer<T>,
    json: Json = Json { ignoreUnknownKeys = true },
): T {
    val element: JsonObject = buildJsonObject {
        put("objectID", JsonPrimitive(this@deserialize.objectID))
        this@deserialize.additionalProperties?.forEach { (key, value) -> put(key, value) }
        this@deserialize.highlightResult?.let { put("_highlightResult", json.encodeToJsonElement(it)) }
        this@deserialize.snippetResult?.let { put("_snippetResult", json.encodeToJsonElement(it)) }
        this@deserialize.rankingInfo?.let { put("_rankingInfo", json.encodeToJsonElement(it)) }
        this@deserialize.distinctSeqID?.let { put("_distinctSeqID", json.encodeToJsonElement(it)) }
    }
    return json.decodeFromJsonElement(serializer, element)
}

