package com.algolia.instantsearch.demo.guide

import com.algolia.instantsearch.core.highlighting.HighlightedString
import com.algolia.instantsearch.helper.highlighting.Highlightable
import com.algolia.search.model.Attribute
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonObject


@Serializable
data class Product(
    val name: String,
    override val _highlightResult: JsonObject?
) : Highlightable {

    @Transient
    public val highlightedName: HighlightedString?
        get() = getHighlight(Attribute("name"))
}