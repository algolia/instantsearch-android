package com.algolia.instantsearch.extension

internal fun <T> tryOrNull(block: () -> T): T? {
    return try {
        block()
    } catch (e: Throwable) {
        null
    }
}

internal expect fun printError(message: String)
