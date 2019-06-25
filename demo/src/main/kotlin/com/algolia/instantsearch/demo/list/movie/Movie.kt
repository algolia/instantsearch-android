package com.algolia.instantsearch.demo.list.movie

import com.algolia.instantsearch.helper.highlighting.Highlighter
import com.algolia.search.model.Attribute
import com.algolia.search.model.ObjectID
import com.algolia.search.model.indexing.Indexable
import com.algolia.search.serialize.Key_HighlightResult
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonObject


@Serializable
data class Movie(
    val title: String,
    val year: String,
    val genre: List<String>,
    val image: String,
    @SerialName(Key_HighlightResult) private val highlightResult: JsonObject? = null,
    override val objectID: ObjectID
) : Indexable {

    @Transient
    public val titleHighlight
        get() = Highlighter.getHighlight(Attribute("title"), highlightResult)
}