package com.algolia.instantsearch.insights.internal.logging

import android.util.Log

internal actual object InsightsLogger {

    private const val TAG = "Algolia Insights"
    actual var enabled: MutableMap<String, Boolean> = mutableMapOf()

    actual fun log(indexName: String, message: String) {
        if (enabled[indexName] == true) {
            Log.d(TAG, "Index=$indexName: $message")
        }
    }

    actual fun log(message: String) {
        Log.d(TAG, message)
    }
}
