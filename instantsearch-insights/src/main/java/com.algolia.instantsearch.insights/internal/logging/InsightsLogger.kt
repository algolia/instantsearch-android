package com.algolia.instantsearch.insights.internal.logging

internal object InsightsLogger {

    private const val TAG = "Algolia Insights"
    var enabled: MutableMap<String, Boolean> = mutableMapOf()

    fun log(indexName: String, message: String) {
        if (enabled[indexName] == true) {
            logd(TAG, "Index=$indexName: $message")
        }
    }

    fun log(message: String) {
        logd(TAG, message)
    }
}
