package com.algolia.instantsearch.helper.highlighting

import com.algolia.instantsearch.core.highlighting.HighlightTokenizer
import com.algolia.instantsearch.core.highlighting.HighlightedString
import com.algolia.search.model.Attribute
import com.algolia.search.model.response.ResponseSearch
import kotlinx.serialization.json.JsonObject


class Highlighter {

    fun getHighlight(
        attribute: Attribute,
        hit: ResponseSearch.Hit
    ): HighlightedString? {
        val highlightResult: JsonObject = hit.highlightResult
        val highlightedAttribute = highlightResult[attribute.raw]

        highlightedAttribute?.let {
            val value = it.jsonObject["value"]
            return HighlightTokenizer().tokenize(value.toString())
        }
        return null
    }
}