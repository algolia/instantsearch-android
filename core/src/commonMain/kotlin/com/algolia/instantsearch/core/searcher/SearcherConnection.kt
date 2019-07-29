package com.algolia.instantsearch.core.searcher

import com.algolia.instantsearch.core.connection.Connection


public fun <T, R> Searcher<R>.connectionView(view: (T) -> Unit, transform: (R?) -> T): Connection {
    return SearcherConnectionView(this, view, transform)
}