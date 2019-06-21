package com.algolia.instantsearch.core.highlighting


/**
 * A token from a highlighted string.
 */
public data class HighlightToken(val content: String, val highlighted: Boolean = false) {
    override fun toString(): String = if (highlighted) "_${content}_" else content
}