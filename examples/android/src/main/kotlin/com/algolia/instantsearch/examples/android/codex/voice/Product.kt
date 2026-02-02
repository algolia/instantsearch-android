package com.algolia.instantsearch.examples.android.codex.voice

import com.algolia.instantsearch.core.Indexable
import com.algolia.instantsearch.highlighting.Highlightable
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class Product(
    val name: String,
    val description: String,
    val image: String,
    override val objectID: String,
    override val _highlightResult: JsonObject?
) : Indexable, Highlightable {

    val highlightedName
        get() = getHighlight("name")

    val highlightedDescription
        get() = getHighlight("description")
}
