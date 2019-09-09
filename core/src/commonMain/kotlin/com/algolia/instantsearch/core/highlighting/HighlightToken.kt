package com.algolia.instantsearch.core.highlighting

import kotlin.jvm.JvmOverloads


/**
 * A token from a highlighted string, with some [content] that might be [highlighted].
 */
public data class HighlightToken @JvmOverloads public constructor(
    val content: String,
    val highlighted: Boolean = false
)