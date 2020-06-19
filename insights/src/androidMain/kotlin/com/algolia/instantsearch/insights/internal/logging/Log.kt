package com.algolia.instantsearch.insights.internal.logging

import android.util.Log

internal actual fun logd(tag: String?, msg: String) {
    Log.d(tag, msg)
}
