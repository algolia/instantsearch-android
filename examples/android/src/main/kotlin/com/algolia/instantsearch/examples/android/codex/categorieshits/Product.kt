package com.algolia.instantsearch.examples.android.codex.categorieshits

import com.algolia.instantsearch.highlighting.Highlightable
import com.algolia.search.model.Attribute
import com.algolia.search.model.ObjectID
import com.algolia.search.model.indexing.Indexable
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class Product(
    val name: String,
    val description: String,
    val image: String,
    override val objectID: ObjectID,
    override val _highlightResult: JsonObject?
) : Indexable, Highlightable {

    val highlightedName
        get() = getHighlight(String("name"))

    val highlightedDescription
        get() = getHighlight(String("description"))
}
