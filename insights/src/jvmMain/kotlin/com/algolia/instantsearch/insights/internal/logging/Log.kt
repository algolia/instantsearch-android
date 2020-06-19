package com.algolia.instantsearch.insights.internal.logging

/**
 * Debug logging.
 */
internal actual fun logd(tag: String?, msg: String) {
    val tagged = tag?.let { "[$it] " }
    println("$tagged$msg")
}
