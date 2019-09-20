package com.algolia.instantsearch.helper.highlighting

import com.algolia.instantsearch.core.highlighting.DefaultPostTag
import com.algolia.instantsearch.core.highlighting.DefaultPreTag
import com.algolia.instantsearch.core.highlighting.HighlightTokenizer
import com.algolia.instantsearch.core.highlighting.HighlightedString
import com.algolia.search.model.Attribute
import com.algolia.search.serialize.toHighlight
import com.algolia.search.serialize.toHighlights
import kotlinx.serialization.json.JsonObject
import kotlin.jvm.JvmName


/**
 * Inheritors of this interface can use [getHighlight]/[getHighlights] methods to render highlights easily.
 */
public interface Highlightable {

    @Suppress("PropertyName") // Else implementers have to remember to specify @SerialName
    public val _highlightResult: JsonObject?

    //  FIXME: Use @JvmDefault to make Highlightable usable for Java users
    public fun getHighlight(
        key: Attribute,
        findHighlight: (JsonObject) -> JsonObject = { it },
        preTag: String = DefaultPreTag,
        postTag: String = DefaultPostTag
    ): HighlightedString? = getHighlight(key.raw, findHighlight, preTag, postTag)

    public fun getHighlight(
        key: String,
        findHighlight: (JsonObject) -> JsonObject = { it },
        preTag: String = DefaultPreTag,
        postTag: String = DefaultPostTag
    ): HighlightedString? = _highlightResult?.let { findHighlight(it).toHighlight(key) }?.let {
        HighlightTokenizer(preTag, postTag)(it.value)
    }

    public fun getHighlights(
        key: Attribute,
        findHighlight: (JsonObject) -> JsonObject = { it },
        preTag: String = DefaultPreTag,
        postTag: String = DefaultPostTag
    ): List<HighlightedString>? = getHighlights(key.raw, findHighlight, preTag, postTag)

    public fun getHighlights(
        key: String,
        findHighlight: (JsonObject) -> JsonObject = { it },
        preTag: String = DefaultPreTag,
        postTag: String = DefaultPostTag
    ): List<HighlightedString>? = _highlightResult?.let { findHighlight(it).toHighlights(key) }?.map {
        HighlightTokenizer(preTag, postTag)(it.value)
    }
}