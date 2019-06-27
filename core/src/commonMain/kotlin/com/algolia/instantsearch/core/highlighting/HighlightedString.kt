package com.algolia.instantsearch.core.highlighting


public data class HighlightedString(
    val original: String,
    val parts: List<HighlightToken>
) : Comparable<HighlightedString> {
    public val highlightedParts
        get() = parts.filter { it.highlighted }.map { it.content }

    override fun compareTo(other: HighlightedString): Int {
        return original.compareTo(other.original)
    }

    override fun toString(): String = parts.joinToString("") { if (it.highlighted) "_${it.content}_" else it.content }
}