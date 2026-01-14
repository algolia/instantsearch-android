package com.algolia.instantsearch.highlighting

import com.algolia.client.model.search.HighlightResultOption
import com.algolia.instantsearch.core.highlighting.DefaultPostTag
import com.algolia.instantsearch.core.highlighting.DefaultPreTag
import com.algolia.instantsearch.core.highlighting.HighlightTokenizer
import com.algolia.instantsearch.core.highlighting.HighlightedString
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray

/**
 * Inheritors of this interface can use [getHighlight]/[getHighlights] methods to render highlights easily.
 */
public interface Highlightable {

    @Suppress("PropertyName") // Else implementers have to remember to specify @SerialName
    public val _highlightResult: JsonObject?

    public fun getHighlight(
        key: String,
        findHighlight: (JsonObject) -> JsonObject = { it },
        preTag: String = DefaultPreTag,
        postTag: String = DefaultPostTag,
    ): HighlightedString? = _highlightResult?.let { findHighlight(it).toHighlight(key) }?.let {
        HighlightTokenizer(preTag, postTag)(it.value)
    }

    public fun getHighlights(
        key: String,
        findHighlight: (JsonObject) -> JsonObject = { it },
        preTag: String = DefaultPreTag,
        postTag: String = DefaultPostTag,
    ): List<HighlightedString>? = _highlightResult?.let { findHighlight(it).toHighlights(key) }?.map {
        HighlightTokenizer(preTag, postTag)(it.value)
    }
}

public fun JsonObject.toHighlights(key: String): List<HighlightResultOption>? {
    return this[key]?.jsonArray?.let { Json.decodeFromJsonElement(ListSerializer(HighlightResultOption.serializer()), it) }
}
