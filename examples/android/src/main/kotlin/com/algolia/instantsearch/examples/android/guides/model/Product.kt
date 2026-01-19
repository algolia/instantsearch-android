package com.algolia.instantsearch.examples.android.guides.model

import com.algolia.instantsearch.core.Indexable
import com.algolia.instantsearch.core.highlighting.HighlightedString
import com.algolia.instantsearch.highlighting.Highlightable
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class Product(
    val name: String,
    val image: String,
    val price: Double,
    val description: String,
    override val objectID: String,
    override val _highlightResult: JsonObject?
) : Indexable, Highlightable {

    val highlightedName: HighlightedString?
        get() = getHighlight("name")
}
