package com.algolia.instantsearch.insights

import android.util.Log


internal object Logger {

    var enabled: Boolean = false

    fun log(indexName: String, message: String) {
        Log.d(indexName, message)
    }
}
