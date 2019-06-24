package com.algolia.instantsearch.helper.highlighting

import com.algolia.instantsearch.core.highlighting.HighlightTokenizer
import com.algolia.instantsearch.core.highlighting.HighlightedString
import com.algolia.search.model.Attribute
import com.algolia.search.model.response.ResponseSearch
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.content


class Highlighter {

    /**
     * Creates a [HighlightedString] from the [hit]'s [attribute].
     *
     * @return null if no `_highlightResult` was found.
     */
    fun getHighlight(
        attribute: Attribute,
        hit: ResponseSearch.Hit
    ): HighlightedString? {

        return getHighlight(attribute, hit.highlightResultOrNull)
    }

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