package com.algolia.instantsearch.demo.list.movie

import com.algolia.instantsearch.core.highlighting.HighlightedString
import com.algolia.instantsearch.helper.highlighting.Highlighter
import com.algolia.search.model.Attribute
import com.algolia.search.model.ObjectID
import com.algolia.search.model.indexing.Indexable
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonObject

/* FIXME: How can we provide a simple API to users that use @Serializable objects?

   - 1st idea: private val _highlightResult + highlights get() = Highlighter.getHighlights(_highlightResult)

   - 2nd idea: abstract Hit with _highlightResult + highlights, then Movie(objectID): Hit(objectID)
*/
interface Highlightable {
    @Suppress("PropertyName") // Else implementers have to specify @SerialName
    val _highlightResult: JsonObject?

    @Transient
    public val highlights: Map<Attribute, List<HighlightedString>>?
        get() {
            val highlights1 = Highlighter.getHighlights(_highlightResult)
            println("HS: $highlights1")
            return highlights1
        }

    fun getHighlightsOrNull(attribute: Attribute): List<HighlightedString>? = highlights?.get(attribute)

    fun getHighlightOrNull(attribute: Attribute): HighlightedString? = highlights?.get(attribute)?.first()

    fun getHighlights(attribute: Attribute): List<HighlightedString> =
        getHighlightsOrNull(attribute) ?: throw IllegalStateException(
            "No highlights for attribute \"$attribute\". Is it in `searchableAttributes`, and in `attributesToHighlight` if specified?"
        )

    fun getHighlight(attribute: Attribute): HighlightedString =
        getHighlightOrNull(attribute) ?: throw IllegalStateException(
            "No highlight for attribute \"$attribute\". Is it in `searchableAttributes`, and in `attributesToHighlight` if specified?"
        )
}


@Serializable
data class Movie(
    val title: String,
    val year: String,
    val genre: List<String>,
    val image: String,
    override val objectID: ObjectID,
    override val _highlightResult: JsonObject?
) : Indexable, Highlightable {

    init {
        if (objectID.raw == "stubObjectID") {
            throw IllegalStateException("Stub objID!")
        }
    }

    @Transient
    public val highlightedTitle
        get() = getHighlight(Attribute("title"))

    @Transient
    public val highlightedGenres: List<HighlightedString>
        get() = if (genre.isNotEmpty()) getHighlights(Attribute("genre")) else emptyList()
}