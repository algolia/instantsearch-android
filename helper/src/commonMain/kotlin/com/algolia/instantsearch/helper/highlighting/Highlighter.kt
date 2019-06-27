package com.algolia.instantsearch.helper.highlighting

import com.algolia.instantsearch.core.highlighting.DefaultPostTag
import com.algolia.instantsearch.core.highlighting.DefaultPreTag
import com.algolia.instantsearch.core.highlighting.HighlightTokenizer
import com.algolia.instantsearch.core.highlighting.HighlightedString
import com.algolia.search.model.Attribute
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.serialize.toHighlight
import com.algolia.search.serialize.toHighlights
import kotlinx.serialization.json.JsonObject

/**
 * Creates a [HighlightedString] from this hit's [attribute].
 *
 * @return null if no `_highlightResult` was found.
 */
fun ResponseSearch.Hit.toHighlightedString(
    attribute: Attribute,
    preTag: String = DefaultPreTag,
    postTag: String = DefaultPostTag
): HighlightedString? = toHighlightedStrings(attribute, preTag, postTag)?.first()

fun ResponseSearch.Hit.toHighlightedStrings(
    attribute: Attribute,
    preTag: String = DefaultPreTag,
    postTag: String = DefaultPostTag
): List<HighlightedString>? {
    return Highlighter.getHighlights(attribute, highlightResultOrNull, preTag, postTag)
}

object Highlighter {

    /**
     * Creates a map of [Attribute] to [HighlightedString] from a [highlightResult].
     *
     * @return null if no `_highlightResult` was found.
     */
    fun getHighlights(
        highlightResult: JsonObject?,
        preTag: String = DefaultPreTag,
        postTag: String = DefaultPostTag
    ): Map<Attribute, List<HighlightedString>>? = highlightResult?.let {
        val tokenizer = HighlightTokenizer(preTag, postTag)

        mutableMapOf<Attribute, List<HighlightedString>>().apply {
            it.keys.forEach { attribute ->
                highlightResult.toHighlights(attribute)
                    ?: highlightResult.toHighlight(attribute)?.let { listOf(it) }?.let { list ->
                        put(Attribute(attribute), list.map { tokenizer(it.value) })
                    }
            }
        }
    }

    /**
     * Creates a list of [HighlightedString] from a [highlightResult]'s [attribute].
     *
     * @return null if no `_highlightResult` was found.
     */
    fun getHighlights(
        attribute: Attribute,
        highlightResult: JsonObject?,
        preTag: String = DefaultPreTag,
        postTag: String = DefaultPostTag
    ): List<HighlightedString>? {

        return (highlightResult?.toHighlights(attribute.raw)
            ?: highlightResult?.toHighlight(attribute.raw)?.let { listOf(it) })
            ?.map { HighlightTokenizer(preTag, postTag)(it.value) }
    }
}