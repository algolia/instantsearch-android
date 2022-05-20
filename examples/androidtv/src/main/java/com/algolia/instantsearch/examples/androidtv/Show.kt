package com.algolia.instantsearch.examples.androidtv

import com.algolia.instantsearch.highlighting.Highlightable
import com.algolia.search.model.Attribute
import com.algolia.search.model.ObjectID
import com.algolia.search.model.indexing.Indexable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class Show(
    val title: String,
    val genres: List<String>,
    @SerialName("poster_path") val posterPath: String,
    @SerialName("vote_average") val voteAverage: Float,
    override val objectID: ObjectID,
    override val _highlightResult: JsonObject?,
) : Indexable, Highlightable {

    val highlightedTitle
        get() = getHighlight(Attribute("title"))

    val posterUrl
        get() = "https://image.tmdb.org/t/p/w500/$posterPath"

    companion object {
        val attributes = listOf(
            Attribute("title"),
            Attribute("genres"),
            Attribute("poster_path")
        )
    }
}
