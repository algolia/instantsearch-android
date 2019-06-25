package com.algolia.instantsearch.core.highlighting


public data class HighlightedString(val original: String, val parts: List<HighlightToken>) {

    public val highlightedParts
        get() = parts.filter { it.highlighted }.map { it.content }

    override fun toString(): String = parts
        .map { if (it.highlighted) "_${it.content}_" else it.content }.toString()
}
