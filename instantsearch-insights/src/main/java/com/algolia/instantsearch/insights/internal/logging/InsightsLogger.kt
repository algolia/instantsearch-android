package com.algolia.instantsearch.insights.internal.logging

import com.algolia.search.model.IndexName

internal object InsightsLogger {

    private const val TAG = "Algolia Insights"
    var enabled: MutableMap<IndexName, Boolean> = mutableMapOf()

    fun log(indexName: IndexName, message: String) {
        if (enabled[indexName] == true) {
            logd(TAG, "Index=$indexName: $message")
        }
    }

    fun log(message: String) {
        logd(TAG, message)
    }
}
