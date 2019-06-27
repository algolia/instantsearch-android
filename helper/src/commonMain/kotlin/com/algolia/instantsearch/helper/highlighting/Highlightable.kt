package com.algolia.instantsearch.helper.highlighting

import com.algolia.instantsearch.core.highlighting.HighlightedString
import com.algolia.search.model.Attribute
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonObject


interface Highlightable {
    @Suppress("PropertyName") // Else implementers have to remember to specify @SerialName
    val _highlightResult: JsonObject?

    @Transient
    public val highlights: Map<Attribute, List<HighlightedString>>?
        get() = Highlighter.getHighlights(_highlightResult)

    fun getHighlightsOrNull(attribute: Attribute): List<HighlightedString>? = highlights?.get(attribute)

    fun getHighlightOrNull(attribute: Attribute): HighlightedString? = highlights?.get(attribute)?.first()

    fun getHighlights(attribute: Attribute): List<HighlightedString> =
        getHighlightsOrNull(attribute) ?: throw IllegalStateException(
            "No highlights for attribute \"$attribute\". Is it in `searchableAttributes`, and in `attributesToHighlight` if specified?"
        )

    fun getHighlight(attribute: Attribute): HighlightedString =
        getHighlightOrNull(attribute) ?: throw IllegalStateException(
            "No highlight for attribute \"$attribute\". Is it in `searchableAttributes`, and in `attributesToHighlight` if specified?"
        )
}


