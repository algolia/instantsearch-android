package com.algolia.instantsearch.insights.internal.extension

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement

internal val JsonNonStrict = Json {
    ignoreUnknownKeys = true
    isLenient = true
    allowSpecialFloatingPointValues = true
    encodeDefaults = true
}

internal fun JsonElement?.deserializeString(): String {
    return if (this != null) JsonNonStrict.decodeFromJsonElement(this) else error("null json element")
}
