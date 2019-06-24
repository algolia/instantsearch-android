package com.algolia.instantsearch.helper.highlighting

import com.algolia.instantsearch.core.highlighting.HighlightTokenizer
import com.algolia.instantsearch.core.highlighting.HighlightedString
import com.algolia.search.model.Attribute
import com.algolia.search.model.response.ResponseSearch
import kotlinx.serialization.json.content


class Highlighter {

    fun getHighlight(
        attribute: Attribute,
        hit: ResponseSearch.Hit
    ): HighlightedString? {

        return hit.highlightResult[attribute.raw]?.jsonObject?.get("value")?.content?.let {
            HighlightTokenizer().tokenize(it)
        }
    }
}