package com.algolia.instantsearch.example.wearos

import com.algolia.instantsearch.core.Indexable
import com.algolia.instantsearch.highlighting.Highlightable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
internal data class Show(
    val title: String,
    val genres: List<String>,
    @SerialName("poster_path") val posterPath: String,
    override val objectID: String,
    override val _highlightResult: JsonObject?
) : Indexable, Highlightable {

    val highlightedTitle
        get() = getHighlight("title")

    val posterUrl
        get() = "https://image.tmdb.org/t/p/w500/$posterPath"

    companion object {
        val attributes = listOf(
            "title",
            "genres",
            "poster_path",
        )
    }
}
