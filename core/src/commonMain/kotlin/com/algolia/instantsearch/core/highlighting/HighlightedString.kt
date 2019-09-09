package com.algolia.instantsearch.core.highlighting


/**
 * A highlighted string, represented as a list of [tokens] along its [original] string.
 */
public data class HighlightedString(
    val original: String,
    val tokens: List<HighlightToken>
) : Comparable<HighlightedString> {

    public val highlightedTokens
        get() = tokens.filter { it.highlighted }.map { it.content }

    override fun compareTo(other: HighlightedString): Int {
        return original.compareTo(other.original)
    }
}