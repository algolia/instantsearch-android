package com.algolia.instantsearch.extension

import com.algolia.instantsearch.InternalInstantSearch

@InternalInstantSearch
public expect object Console {

    public fun debug(message: String, throwable: Throwable? = null)

    public fun info(message: String, throwable: Throwable? = null)

    public fun warn(message: String, throwable: Throwable? = null)

    public fun error(message: String, throwable: Throwable? = null)
}
