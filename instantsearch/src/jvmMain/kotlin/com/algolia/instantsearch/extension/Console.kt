package com.algolia.instantsearch.extension

import com.algolia.instantsearch.InternalInstantSearch
import java.util.logging.Level
import java.util.logging.Logger

@InternalInstantSearch
public actual object Console {

    private val logger = Logger.getLogger(Console::class.qualifiedName)

    public actual fun debug(message: String, throwable: Throwable?) {
        logger.log(Level.FINE, message, throwable)
    }

    public actual fun info(message: String, throwable: Throwable?) {
        logger.log(Level.INFO, message, throwable)
    }

    public actual fun warn(message: String, throwable: Throwable?) {
        logger.log(Level.WARNING, message, throwable)
    }

    public actual fun error(message: String, throwable: Throwable?) {
        logger.log(Level.SEVERE, message, throwable)
    }
}
