package com.algolia.instantsearch.insights.internal

import android.util.Log

actual class Log {
    actual companion object {
        actual fun d(tag: String?, msg: String) {
            Log.d(tag, msg)
        }
    }
}
