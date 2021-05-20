package com.algolia.instantsearch.core.searcher

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.searcher.internal.SearcherConnectionView

public fun <T, R> Searcher<R>.connectView(view: (T) -> Unit, transform: (R?) -> T): Connection {
    return SearcherConnectionView(this, view, transform)
}
