package com.algolia.instantsearch.example.wearos

import com.algolia.instantsearch.highlighting.Highlightable
import com.algolia.search.model.Attribute
import com.algolia.search.model.ObjectID
import com.algolia.search.model.indexing.Indexable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
internal data class Show(
    val title: String,
    val genres: List<String>,
    @SerialName("poster_path") val posterPath: String,
    override val objectID: ObjectID,
    override val _highlightResult: JsonObject?
) : Indexable, Highlightable {

    val highlightedTitle
        get() = getHighlight(String("title"))

    val posterUrl
        get() = "https://image.tmdb.org/t/p/w500/$posterPath"

    companion object {
        val attributes = listOf(
            String("title"),
            String("genres"),
            String("poster_path")
        )
    }
}
