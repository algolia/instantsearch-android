package com.algolia.instantsearch.extension

import android.util.Log

internal actual fun printError(message: String) {
    Log.e("InstantSearch", message)
}
