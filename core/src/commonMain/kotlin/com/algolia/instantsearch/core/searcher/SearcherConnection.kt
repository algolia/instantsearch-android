package com.algolia.instantsearch.core.searcher

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.Connection


public fun <T, R> Searcher<R>.connectView(view: Callback<T>, transform: (R?) -> T): Connection {
    return SearcherConnectionView(this, view, transform)
}