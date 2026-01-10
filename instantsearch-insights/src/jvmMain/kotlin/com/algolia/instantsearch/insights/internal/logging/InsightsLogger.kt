package com.algolia.instantsearch.insights.internal.logging

import com.algolia.instantsearch.insights.Insights
import com.algolia.instantsearch.migration2to3.IndexName
import org.slf4j.LoggerFactory

internal actual object InsightsLogger {

    private val logger = LoggerFactory.getLogger(Insights::class.java)
    actual var enabled: MutableMap<IndexName, Boolean> = mutableMapOf()

    actual fun log(indexName: IndexName, message: String) {
        if (enabled[indexName] == true) {
            logger.debug("Index=$indexName: $message")
        }
    }

    actual fun log(message: String) {
        logger.debug(message)
    }
}
