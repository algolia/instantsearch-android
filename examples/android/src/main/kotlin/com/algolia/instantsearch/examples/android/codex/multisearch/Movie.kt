package com.algolia.instantsearch.examples.android.codex.multisearch

import com.algolia.instantsearch.core.Indexable
import com.algolia.instantsearch.highlighting.Highlightable
import com.algolia.search.model.Attribute
import com.algolia.search.model.ObjectID
import com.algolia.search.model.indexing.Indexable
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class Movie(
    val title: String,
    override val objectID: String,
    override val _highlightResult: JsonObject?
) : Indexable, Highlightable {

    val highlightedTitle
        get() = getHighlight("title")
}
