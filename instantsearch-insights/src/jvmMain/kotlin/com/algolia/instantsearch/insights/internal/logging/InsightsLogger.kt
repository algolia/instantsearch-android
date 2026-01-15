package com.algolia.instantsearch.insights.internal.logging

import com.algolia.instantsearch.insights.Insights
import org.slf4j.LoggerFactory

internal actual object InsightsLogger {

    private val logger = LoggerFactory.getLogger(Insights::class.java)
    actual var enabled: MutableMap<String, Boolean> = mutableMapOf()

    actual fun log(indexName: String, message: String) {
        if (enabled[indexName] == true) {
            logger.debug("Index=$indexName: $message")
        }
    }

    actual fun log(message: String) {
        logger.debug(message)
    }
}
