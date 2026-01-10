package com.algolia.instantsearch.migration2to3

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@InternalSerializationApi
@Serializable
public data class HighlightResult(
    /**
     * Markup text with occurrences highlighted. The tags used for highlighting are specified via [Query.highlightPreTag]
     * and [Query.highlightPostTag]
     */
    @SerialName(Key.Value) val value: String,
    /**
     * Indicates how well the value matched the search query.
     */
    @SerialName(Key.MatchLevel) val matchLevel: MatchLevel,
    /**
     * List of words from the query that matched the object.
     */
    @SerialName(Key.MatchedWords) val matchedWords: List<String>,
    /**
     * Whether the entire value is highlighted.
     */
    @SerialName(Key.FullyHighlighted) val fullyHighlighted: Boolean? = null
)

