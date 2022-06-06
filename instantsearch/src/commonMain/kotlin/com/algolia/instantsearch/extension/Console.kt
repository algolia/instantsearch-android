package com.algolia.instantsearch.extension

internal expect object Console {

    fun debug(message: String, throwable: Throwable?)

    fun info(message: String, throwable: Throwable?)

    fun warn(message: String, throwable: Throwable?)

    fun error(message: String, throwable: Throwable?)
}
