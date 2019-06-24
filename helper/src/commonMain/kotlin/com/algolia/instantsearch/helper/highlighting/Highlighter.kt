package com.algolia.instantsearch.helper.highlighting

import com.algolia.instantsearch.core.highlighting.HighlightTokenizer
import com.algolia.instantsearch.core.highlighting.HighlightedString
import com.algolia.search.model.Attribute
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.HighlightResult
import kotlinx.serialization.json.content


fun HighlightResult.toHighlightedString(): HighlightedString {
    return HighlightTokenizer().tokenize(value)
}

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

        return hit.highlightResultOrNull?.get(attribute.raw)?.jsonObject?.get("value")?.content?.let {
            HighlightTokenizer().tokenize(it)
        }
    }
}