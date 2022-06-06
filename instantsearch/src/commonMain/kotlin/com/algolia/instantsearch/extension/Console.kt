package com.algolia.instantsearch.extension

internal expect object Console {

    fun debug(message: String, throwable: Throwable? = null)

    fun info(message: String, throwable: Throwable? = null)

    fun warn(message: String, throwable: Throwable? = null)

    fun error(message: String, throwable: Throwable? = null)
}
