package com.algolia.instantsearch.extension

import java.util.logging.Level
import java.util.logging.Logger

internal actual object Console {

    private const val Tag = "InstantSearch"
    private val logger = Logger.getLogger(Console::class.qualifiedName)

    actual fun debug(message: String, throwable: Throwable?) {
        logger.log(Level.FINE, message, throwable)
    }

    actual fun info(message: String, throwable: Throwable?) {
        logger.log(Level.INFO, message, throwable)
    }

    actual fun warn(message: String, throwable: Throwable?) {
        logger.log(Level.WARNING, message, throwable)
    }

    actual fun error(message: String, throwable: Throwable?) {
        logger.log(Level.SEVERE, message, throwable)
    }

}
