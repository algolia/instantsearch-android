package com.algolia.instantsearch.helper.highlighting

import com.algolia.instantsearch.core.highlighting.HighlightTokenizer
import com.algolia.instantsearch.core.highlighting.HighlightedString
import com.algolia.search.model.Attribute
import com.algolia.search.model.response.ResponseSearch
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.content

/**
 * Creates a [HighlightedString] from this hit's [attribute].
 *
 * @return null if no `_highlightResult` was found.
 */
fun ResponseSearch.Hit.toHighlightedString(attribute: Attribute): HighlightedString? {
    return Highlighter.getHighlight(attribute, highlightResultOrNull)
}

object Highlighter {

    /**
     * Creates a [HighlightedString] from a [highlightResult]'s [attribute].
     *
     * @return null if no `_highlightResult` was found.
     */
    fun getHighlight(
        attribute: Attribute,
        highlightResult: JsonObject?
    ): HighlightedString? {

        return highlightResult?.get(attribute.raw)?.jsonObject?.get("value")?.content?.let {
            HighlightTokenizer()(it)
        }
    }
}