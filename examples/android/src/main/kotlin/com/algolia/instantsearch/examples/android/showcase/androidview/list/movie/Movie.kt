package com.algolia.instantsearch.examples.android.showcase.androidview.list.movie

import com.algolia.instantsearch.core.Indexable
import com.algolia.instantsearch.highlighting.Highlightable
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject


@Serializable
data class Movie(
    val title: String,
    val year: String,
    val genre: List<String>,
    val image: String,
    override val objectID: String,
    override val _highlightResult: JsonObject?
) : Indexable, Highlightable {

    public val highlightedTitle
        get() = getHighlight("title")

    public val highlightedGenres
        get() = getHighlights("genre")

    public val highlightedActors
        get() = getHighlights("actors")
}
