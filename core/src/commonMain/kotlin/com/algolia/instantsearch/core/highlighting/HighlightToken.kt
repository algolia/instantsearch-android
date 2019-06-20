package com.algolia.instantsearch.core.highlighting


/**
 * A token from a highlighted string.
 */
public data class HighlightToken(val highlighted: Boolean = false, val content: String)
