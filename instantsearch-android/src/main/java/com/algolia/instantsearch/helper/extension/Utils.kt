package com.algolia.instantsearch.helper.extension

internal fun <T> tryOrNull(block: () -> T): T? {
    return try {
        block()
    } catch (e: Throwable) {
        null
    }
}
