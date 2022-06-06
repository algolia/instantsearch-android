package com.algolia.instantsearch.extension

import android.util.Log

internal actual object Console {

    private const val Tag = "InstantSearch"

    actual fun debug(message: String, throwable: Throwable?) {
        Log.d(Tag, message, throwable)
    }

    actual fun info(message: String, throwable: Throwable?) {
        Log.i(Tag, message, throwable)
    }

    actual fun warn(message: String, throwable: Throwable?) {
        Log.w(Tag, message, throwable)
    }

    actual fun error(message: String, throwable: Throwable?) {
        Log.e(Tag, message, throwable)
    }
}
