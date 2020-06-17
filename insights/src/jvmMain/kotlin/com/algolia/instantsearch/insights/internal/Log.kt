package com.algolia.instantsearch.insights.internal

actual class Log {

    actual companion object {

        @JvmStatic
        actual fun d(tag: String?, msg: String) {
            val tagged = tag?.let { "[$it] " }
            println("$tagged$msg")
        }
    }
}
