package com.algolia.instantsearch.extension

import android.util.Log
import com.algolia.instantsearch.InternalInstantSearch

@InternalInstantSearch
public actual object Console {

    private const val Tag = "InstantSearch"

    public actual fun debug(message: String, throwable: Throwable?) {
        Log.d(Tag, message, throwable)
    }

    public actual fun info(message: String, throwable: Throwable?) {
        Log.i(Tag, message, throwable)
    }

    public actual fun warn(message: String, throwable: Throwable?) {
        Log.w(Tag, message, throwable)
    }

    public actual fun error(message: String, throwable: Throwable?) {
        Log.e(Tag, message, throwable)
    }
}
