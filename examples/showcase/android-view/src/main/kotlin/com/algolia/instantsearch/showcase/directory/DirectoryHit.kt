package com.algolia.instantsearch.showcase.directory

import com.algolia.instantsearch.highlighting.Highlightable
import com.algolia.search.model.Attribute
import com.algolia.search.model.ObjectID
import com.algolia.search.model.indexing.Indexable
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonObject


@Serializable
data class DirectoryHit(
    override val objectID: ObjectID,
    val name: String,
    val type: String,
    val index: String,
    override val _highlightResult: JsonObject? = null
): Indexable, Highlightable {

    public val highlightedName
        get() = getHighlight(Attribute("name"), preTag = "<b>", postTag = "</b>")
}