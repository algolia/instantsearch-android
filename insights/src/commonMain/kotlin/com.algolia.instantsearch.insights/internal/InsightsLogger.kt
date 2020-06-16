package com.algolia.instantsearch.insights.internal

internal object InsightsLogger {

    private const val TAG = "Algolia Insights"
    var enabled: MutableMap<String, Boolean> = mutableMapOf()

    fun log(indexName: String, message: String) {
        if (enabled[indexName] == true) {
            Log.d(TAG, "Index=$indexName: $message")
        }
    }

    fun log(message: String) {
        Log.d(TAG, message)
    }
}

expect class Log {
    companion object {
        fun d(tag: String?, msg: String)
    }
}
