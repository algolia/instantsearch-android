package com.algolia.instantsearch.insights.internal.logging

import android.util.Log
import com.algolia.search.model.IndexName

internal actual object InsightsLogger {

    private const val TAG = "Algolia Insights"
    actual var enabled: MutableMap<IndexName, Boolean> = mutableMapOf()

    actual fun log(indexName: IndexName, message: String) {
        if (enabled[indexName] == true) {
            Log.d(TAG, "Index=$indexName: $message")
        }
    }

    actual fun log(message: String) {
        Log.d(TAG, message)
    }
}
