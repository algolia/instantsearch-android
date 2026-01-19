package com.algolia.instantsearch.examples.android.showcase.androidview.list.suggestion

import com.algolia.instantsearch.core.Indexable
import com.algolia.instantsearch.core.highlighting.HighlightedString
import com.algolia.instantsearch.highlighting.Highlightable
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class Suggestion(
    val query: String,
    override val objectID: String,
    override val _highlightResult: JsonObject?
) : Indexable, Highlightable {

    val highlightedQuery: HighlightedString?
        get() = getHighlight(("query"))
}
