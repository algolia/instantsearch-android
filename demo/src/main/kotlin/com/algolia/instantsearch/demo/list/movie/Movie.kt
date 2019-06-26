package com.algolia.instantsearch.demo.list.movie

import com.algolia.instantsearch.core.highlighting.HighlightedString
import com.algolia.instantsearch.helper.highlighting.Highlighter
import com.algolia.search.model.Attribute
import com.algolia.search.model.ObjectID
import com.algolia.search.model.indexing.Indexable
import com.algolia.search.serialize.Key_HighlightResult
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonObject

/* FIXME: How can we provide a simple API to users that use @Serializable objects?

   - 1st idea: private val highlightResult + highlights get() = Highlighter.getHighlights(highlightResult)

   - 2nd idea: abstract Hit with highlightResult + highlights, then Movie(objectID): Hit(objectID)
*/
@Serializable
abstract class Hit(

    //DISCUSS: can this fake objectID cause problems down the line?
    @Transient override val objectID: ObjectID = ObjectID("stubObjectID")
) : Indexable {
    @SerialName(Key_HighlightResult)

    private val highlightResult: JsonObject? = null

    @Transient
    public val highlights: Map<Attribute, List<HighlightedString>>?
        get() = Highlighter.getHighlights(highlightResult)
}


@Serializable
data class Movie(
    val title: String,
    val year: String,
    val genre: List<String>,
    val image: String,
    override val objectID: ObjectID
) : Hit(objectID) {

    @Transient
    public val titleHighlight
        get() = highlights?.get(Attribute("title"))?.first()

}