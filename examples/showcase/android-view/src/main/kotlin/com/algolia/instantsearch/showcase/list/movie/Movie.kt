package com.algolia.instantsearch.showcase.list.movie

import com.algolia.instantsearch.highlighting.Highlightable
import com.algolia.search.model.Attribute
import com.algolia.search.model.ObjectID
import com.algolia.search.model.indexing.Indexable
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonObject


@Serializable
data class Movie(
    val title: String,
    val year: String,
    val genre: List<String>,
    val image: String,
    override val objectID: ObjectID,
    override val _highlightResult: JsonObject?
) : Indexable, Highlightable {

    public val highlightedTitle
        get() = getHighlight(Attribute("title"))

    public val highlightedGenres
        get() = getHighlights(Attribute("genre"))

    public val highlightedActors
        get() = getHighlights(Attribute("actors"))
}