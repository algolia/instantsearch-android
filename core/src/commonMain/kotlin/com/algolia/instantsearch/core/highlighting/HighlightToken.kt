package com.algolia.instantsearch.core.highlighting

import kotlin.jvm.JvmField
import kotlin.jvm.JvmOverloads


/**
 * A token from a highlighted string, with some [content] that might be [highlighted].
 */
public data class HighlightToken @JvmOverloads public constructor(
    @JvmField
    val content: String,
    @JvmField
    val highlighted: Boolean = false
)