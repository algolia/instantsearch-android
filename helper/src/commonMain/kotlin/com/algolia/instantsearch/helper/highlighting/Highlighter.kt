package com.algolia.instantsearch.helper.highlighting

import com.algolia.instantsearch.core.highlighting.DefaultPostTag
import com.algolia.instantsearch.core.highlighting.DefaultPreTag
import com.algolia.instantsearch.core.highlighting.HighlightTokenizer
import com.algolia.instantsearch.core.highlighting.HighlightedString
import com.algolia.search.model.Attribute
import com.algolia.search.model.response.ResponseSearch
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.content

/**
 * Creates a [HighlightedString] from this hit's [attribute].
 *
 * @return null if no `_highlightResult` was found.
 */
fun ResponseSearch.Hit.toHighlightedString(
    attribute: Attribute,
    preTag: String = DefaultPreTag,
    postTag: String = DefaultPostTag
): HighlightedString? {
    return Highlighter.getHighlight(attribute, highlightResultOrNull, preTag, postTag)
}

object Highlighter {

    /**
     * Creates a map of [Attribute] to [HighlightedString] from a [highlightResult].
     *
     * @return null if no `_highlightResult` was found.
     */
    //FIXME: Can I return something better for the default case than List<HString>?
    fun getHighlights(
        highlightResult: JsonObject?,
        preTag: String = DefaultPreTag,
        postTag: String = DefaultPostTag
    ): Map<Attribute, List<HighlightedString>>? = highlightResult?.let {
        val highlightTokenizer = HighlightTokenizer(preTag, postTag)

        mutableMapOf<Attribute, List<HighlightedString>>().apply {
            it.content.forEach { (attribute, highlightJSON) ->
                put(Attribute(attribute), mutableListOf<HighlightedString>().apply {
                    if (highlightJSON is JsonObject) {
                        highlightJSON.jsonObject["value"]?.content?.let {
                            add(highlightTokenizer(it))
                        }
                    } else if (highlightJSON is JsonArray) {
                        highlightJSON.jsonArray.forEach {
                            it.jsonObject["value"]?.content?.let { content ->
                                add(highlightTokenizer(content))
                            }
                        }
                    }
                })
            }
        }
    }

    /**
     * Creates a [HighlightedString] from a [highlightResult]'s [attribute].
     *
     * @return null if no `_highlightResult` was found.
     */
    //TODO: Handle List attribute, + Test 
    fun getHighlight(
        attribute: Attribute,
        highlightResult: JsonObject?,
        preTag: String = DefaultPreTag,
        postTag: String = DefaultPostTag
    ): HighlightedString? {

        return highlightResult?.get(attribute.raw)?.jsonObject?.get("value")?.content?.let {
            HighlightTokenizer(preTag, postTag)(it)
        }
    }
}