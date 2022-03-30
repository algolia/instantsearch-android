package com.algolia.instantsearch.guides.querysuggestion.product

import com.algolia.instantsearch.core.highlighting.HighlightedString
import com.algolia.instantsearch.highlighting.Highlightable
import com.algolia.search.model.Attribute
import com.algolia.search.model.ObjectID
import com.algolia.search.model.indexing.Indexable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class Product(
    val name: String,
    @SerialName("image_urls") val images: List<String>,
    val price: Price,
    val description: String,
    override val objectID: ObjectID,
    override val _highlightResult: JsonObject?
) : Indexable, Highlightable {

    val highlightedName: HighlightedString?
        get() = getHighlight(Attribute("name"))
}

@Serializable
data class Price(
    val currency: String,
    val value: String,
)
